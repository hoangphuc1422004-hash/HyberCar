package com.porsche.hypercar.presentation.summary

import com.porsche.hypercar.presentation.configurator.EngineOption
import com.porsche.hypercar.presentation.configurator.ExteriorColor
import com.porsche.hypercar.presentation.configurator.InteriorOption
import com.porsche.hypercar.presentation.configurator.PriceLineItem
import com.porsche.hypercar.presentation.configurator.WheelOption

/**
 * UI state for the Summary screen.
 * Populated from the Configurator's finalized state.
 */
data class SummaryUiState(
    val carModel: String = "",
    val selectedEngine: EngineOption = EngineOption.HYBRID_V8,
    val selectedWheels: WheelOption = WheelOption.STANDARD_ALLOY,
    val selectedExteriorColor: ExteriorColor = ExteriorColor.GT_SILVER,
    val selectedInterior: InteriorOption = InteriorOption.STANDARD_LEATHER,
    val isPerformancePackageSelected: Boolean = false,
    val totalPrice: Long = 0L,
    val optionsBreakdown: List<PriceLineItem> = emptyList(),
    val isLoading: Boolean = true
)
