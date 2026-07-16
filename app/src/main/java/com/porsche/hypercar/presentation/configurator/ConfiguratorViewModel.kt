package com.porsche.hypercar.presentation.configurator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.porsche.hypercar.domain.configurator.model.SavedConfiguration
import com.porsche.hypercar.domain.configurator.usecase.CalculateTotalPriceUseCase
import com.porsche.hypercar.domain.configurator.usecase.GetSavedConfigurationUseCase
import com.porsche.hypercar.domain.configurator.usecase.SaveConfigurationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Configurator screen, implementing **Unidirectional Data Flow (UDF)**:
 *
 * ```
 *   ┌──────────┐   Event   ┌───────────────┐   State   ┌──────────┐
 *   │    UI    │ ────────► │  ViewModel    │ ────────► │    UI    │
 *   └──────────┘           │  (this class) │           └──────────┘
 *                          └───────┬───────┘
 *                                  │ Effect (one-shot)
 *                                  ▼
 *                          ┌──────────┐
 *                          │    UI    │  (Navigation, Snackbar, …)
 *                          └──────────┘
 * ```
 *
 * - **State** is exposed as an immutable [StateFlow] of [ConfiguratorUiState].
 * - **Events** are processed through [onEvent].
 * - **Effects** (one-time side effects) are emitted via a [Channel].
 */
@HiltViewModel
class ConfiguratorViewModel @Inject constructor(
    private val calculateTotalPriceUseCase: CalculateTotalPriceUseCase,
    private val saveConfigurationUseCase: SaveConfigurationUseCase,
    private val getSavedConfigurationUseCase: GetSavedConfigurationUseCase
) : ViewModel() {

    // ── State ────────────────────────────────────────────────────────────────
    private val _uiState = MutableStateFlow(ConfiguratorUiState())

    /** Observable, immutable UI state. The single source of truth. */
    val uiState: StateFlow<ConfiguratorUiState> = _uiState.asStateFlow()

    // ── Effects (one-shot) ───────────────────────────────────────────────────
    private val _effects = Channel<ConfiguratorEffect>(Channel.BUFFERED)

    /** Flow of one-time side effects for the UI to collect. */
    val effects = _effects.receiveAsFlow()

    // ── Initialization ───────────────────────────────────────────────────────
    init {
        // Calculate the initial price based on default selections
        recalculatePrice()
    }

    // ═════════════════════════════════════════════════════════════════════════
    // Public API – Event handler
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Single entry point for all user-initiated events.
     * The UI calls this method; the ViewModel decides how to update state.
     */
    fun onEvent(event: ConfiguratorEvent) {
        when (event) {
            is ConfiguratorEvent.SelectEngine          -> handleSelectEngine(event.engine)
            is ConfiguratorEvent.SelectWheels           -> handleSelectWheels(event.wheels)
            is ConfiguratorEvent.SelectExteriorColor    -> handleSelectExteriorColor(event.color)
            is ConfiguratorEvent.SelectInterior         -> handleSelectInterior(event.interior)
            is ConfiguratorEvent.TogglePerformancePackage -> handleTogglePerformance(event.isSelected)
            is ConfiguratorEvent.SaveConfiguration      -> handleSaveConfiguration()
            is ConfiguratorEvent.LoadSavedConfiguration -> handleLoadSavedConfiguration()
            is ConfiguratorEvent.NavigateToSummary      -> handleNavigateToSummary()
            is ConfiguratorEvent.DismissError           -> handleDismissError()
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // Event handlers (private)
    // ═════════════════════════════════════════════════════════════════════════

    private fun handleSelectEngine(engine: EngineOption) {
        _uiState.update { it.copy(selectedEngine = engine) }
        recalculatePrice()
    }

    private fun handleSelectWheels(wheels: WheelOption) {
        _uiState.update { it.copy(selectedWheels = wheels) }
        recalculatePrice()
    }

    private fun handleSelectExteriorColor(color: ExteriorColor) {
        _uiState.update { it.copy(selectedExteriorColor = color) }
        recalculatePrice()
    }

    private fun handleSelectInterior(interior: InteriorOption) {
        _uiState.update { it.copy(selectedInterior = interior) }
        recalculatePrice()
    }

    /**
     * When the Performance Package is toggled OFF, any option that requires it
     * (e.g., Carbon Fiber wheels, Race engine) is automatically reverted
     * to the base option, and an auto-correction effect is emitted.
     */
    private fun handleTogglePerformance(isSelected: Boolean) {
        _uiState.update { current ->
            var updated = current.copy(isPerformancePackageSelected = isSelected)

            if (!isSelected) {
                // ── Auto-correct: Carbon wheels → Standard ──────────────
                if (updated.selectedWheels.requiresPerformancePackage) {
                    updated = updated.copy(selectedWheels = WheelOption.STANDARD_ALLOY)
                    emitEffect(
                        ConfiguratorEffect.ShowConstraintAutoCorrection(
                            "Mâm '${current.selectedWheels.displayName}' đã được đổi về '${WheelOption.STANDARD_ALLOY.displayName}' vì bạn đã tắt gói Performance."
                        )
                    )
                }
                // ── Auto-correct: Race engine → Standard ────────────────
                if (updated.selectedEngine == EngineOption.HYBRID_V8_RACE) {
                    updated = updated.copy(selectedEngine = EngineOption.HYBRID_V8)
                    emitEffect(
                        ConfiguratorEffect.ShowConstraintAutoCorrection(
                            "Động cơ '${current.selectedEngine.displayName}' đã được đổi về '${EngineOption.HYBRID_V8.displayName}' vì bạn đã tắt gói Performance."
                        )
                    )
                }
            }

            updated
        }
        recalculatePrice()
    }

    /**
     * Saves the current configuration to local storage.
     */
    private fun handleSaveConfiguration() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                val state = _uiState.value
                val savedConfig = SavedConfiguration(
                    carModel = state.carModel,
                    basePrice = state.basePrice,
                    selectedEngine = state.selectedEngine,
                    selectedWheels = state.selectedWheels,
                    selectedExteriorColor = state.selectedExteriorColor,
                    selectedInterior = state.selectedInterior,
                    isPerformancePackageSelected = state.isPerformancePackageSelected,
                    totalPrice = state.totalPrice,
                    optionsBreakdown = state.optionsBreakdown
                )
                saveConfigurationUseCase(savedConfig)
                emitEffect(ConfiguratorEffect.ShowSuccessMessage("Cấu hình đã được lưu thành công!"))
            } catch (e: Exception) {
                emitEffect(ConfiguratorEffect.ShowErrorMessage("Lỗi khi lưu cấu hình: ${e.localizedMessage}"))
            } finally {
                _uiState.update { it.copy(isSaving = false) }
            }
        }
    }

    /**
     * Loads a previously saved configuration and restores it to the UI state.
     */
    private fun handleLoadSavedConfiguration() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val saved = getSavedConfigurationUseCase()
                if (saved != null) {
                    _uiState.update {
                        it.copy(
                            carModel = saved.carModel,
                            basePrice = saved.basePrice,
                            selectedEngine = saved.selectedEngine,
                            selectedWheels = saved.selectedWheels,
                            selectedExteriorColor = saved.selectedExteriorColor,
                            selectedInterior = saved.selectedInterior,
                            isPerformancePackageSelected = saved.isPerformancePackageSelected,
                            isLoading = false
                        )
                    }
                    recalculatePrice()
                    emitEffect(ConfiguratorEffect.ShowSuccessMessage("Đã tải cấu hình đã lưu."))
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                    emitEffect(ConfiguratorEffect.ShowErrorMessage("Không tìm thấy cấu hình đã lưu."))
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                emitEffect(ConfiguratorEffect.ShowErrorMessage("Lỗi khi tải cấu hình: ${e.localizedMessage}"))
            }
        }
    }

    private fun handleNavigateToSummary() {
        // Only navigate if there are no constraint violations
        val state = _uiState.value
        if (state.constraintWarnings.isNotEmpty()) {
            emitEffect(
                ConfiguratorEffect.ShowErrorMessage(
                    "Vui lòng giải quyết các cảnh báo ràng buộc trước khi xem tổng quan."
                )
            )
            return
        }
        emitEffect(ConfiguratorEffect.NavigateToSummary)
    }

    private fun handleDismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    // ═════════════════════════════════════════════════════════════════════════
    // Internal helpers
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Delegates to [CalculateTotalPriceUseCase] and applies the result to the state.
     */
    private fun recalculatePrice() {
        val result = calculateTotalPriceUseCase(_uiState.value)
        _uiState.update {
            it.copy(
                totalPrice = result.totalPrice,
                optionsBreakdown = result.lineItems,
                constraintWarnings = result.constraintWarnings
            )
        }
    }

    /**
     * Helper to send a one-time effect.
     */
    private fun emitEffect(effect: ConfiguratorEffect) {
        viewModelScope.launch {
            _effects.send(effect)
        }
    }
}
