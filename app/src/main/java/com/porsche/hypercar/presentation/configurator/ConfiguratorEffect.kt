package com.porsche.hypercar.presentation.configurator

/**
 * Sealed interface representing one-time side effects emitted by the ViewModel.
 *
 * Unlike [ConfiguratorUiState], effects are consumed ONCE by the UI
 * (e.g., navigation, showing a Snackbar, or a Toast).
 *
 * Flow:
 *   ViewModel  ──(Effect)──►  UI (consumed once)
 */
sealed interface ConfiguratorEffect {

    /** Navigate to the Summary screen. */
    data object NavigateToSummary : ConfiguratorEffect

    /** Show a success message (e.g., "Configuration saved!"). */
    data class ShowSuccessMessage(val message: String) : ConfiguratorEffect

    /** Show an error message as a Snackbar or Toast. */
    data class ShowErrorMessage(val message: String) : ConfiguratorEffect

    /**
     * Notify the UI that the user selected an option that violates a constraint,
     * and the system has automatically adjusted their selection.
     * Example: "Mâm carbon đã được đổi về tiêu chuẩn vì bạn tắt gói Performance."
     */
    data class ShowConstraintAutoCorrection(val message: String) : ConfiguratorEffect
}
