package com.porsche.hypercar.presentation.configurator

/**
 * Sealed interface representing all possible user-initiated events
 * on the Configurator screen.
 *
 * In Unidirectional Data Flow:
 *   UI  ──(Event)──►  ViewModel  ──(State)──►  UI
 *
 * Events are immutable intents that describe WHAT the user did,
 * not HOW the state should change.
 */
sealed interface ConfiguratorEvent {

    // ── Engine ────────────────────────────────────────────────────────────────
    /** User selected a different engine option. */
    data class SelectEngine(val engine: EngineOption) : ConfiguratorEvent

    // ── Wheels ────────────────────────────────────────────────────────────────
    /** User selected a different wheel option. */
    data class SelectWheels(val wheels: WheelOption) : ConfiguratorEvent

    // ── Exterior Color ───────────────────────────────────────────────────────
    /** User selected a different exterior color. */
    data class SelectExteriorColor(val color: ExteriorColor) : ConfiguratorEvent

    // ── Interior ─────────────────────────────────────────────────────────────
    /** User selected a different interior option. */
    data class SelectInterior(val interior: InteriorOption) : ConfiguratorEvent

    // ── Performance Package ──────────────────────────────────────────────────
    /** User toggled the Performance Package on or off. */
    data class TogglePerformancePackage(val isSelected: Boolean) : ConfiguratorEvent

    // ── Actions ──────────────────────────────────────────────────────────────
    /** User tapped "Save Configuration". */
    data object SaveConfiguration : ConfiguratorEvent

    /** User tapped "Load Saved Configuration". */
    data object LoadSavedConfiguration : ConfiguratorEvent

    /** User tapped "View Summary" to navigate to the summary screen. */
    data object NavigateToSummary : ConfiguratorEvent

    /** User dismissed an error or warning message. */
    data object DismissError : ConfiguratorEvent
}
