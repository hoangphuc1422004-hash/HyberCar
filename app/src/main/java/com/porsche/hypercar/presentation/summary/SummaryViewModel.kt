package com.porsche.hypercar.presentation.summary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.porsche.hypercar.domain.configurator.usecase.GetSavedConfigurationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Summary screen.
 *
 * Loads the saved (finalized) configuration and exposes it as an immutable
 * [StateFlow] of [SummaryUiState] following Unidirectional Data Flow.
 *
 * The Summary screen is **read-only** — it does not accept user events that
 * modify the configuration. It simply presents the data that was committed
 * in the Configurator.
 */
@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val getSavedConfigurationUseCase: GetSavedConfigurationUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // ── State ────────────────────────────────────────────────────────────────
    private val _uiState = MutableStateFlow(SummaryUiState())

    /** Observable, immutable UI state for the Summary screen. */
    val uiState: StateFlow<SummaryUiState> = _uiState.asStateFlow()

    // ── Initialization ───────────────────────────────────────────────────────
    init {
        loadConfiguration()
    }

    /**
     * Loads the most recently saved configuration and maps it to [SummaryUiState].
     *
     * Flow:
     * ```
     *   GetSavedConfigurationUseCase
     *           │
     *           ▼
     *   SavedConfiguration  ──(map)──►  SummaryUiState
     *           │
     *           ▼
     *       StateFlow  ──►  UI
     * ```
     */
    private fun loadConfiguration() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val saved = getSavedConfigurationUseCase()
                if (saved != null) {
                    _uiState.update {
                        SummaryUiState(
                            carModel = saved.carModel,
                            selectedEngine = saved.selectedEngine,
                            selectedWheels = saved.selectedWheels,
                            selectedExteriorColor = saved.selectedExteriorColor,
                            selectedInterior = saved.selectedInterior,
                            isPerformancePackageSelected = saved.isPerformancePackageSelected,
                            totalPrice = saved.totalPrice,
                            optionsBreakdown = saved.optionsBreakdown,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    /**
     * Allows the UI to explicitly request a reload of the configuration.
     */
    fun refresh() {
        loadConfiguration()
    }
}
