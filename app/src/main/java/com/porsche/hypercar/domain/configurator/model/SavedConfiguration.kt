package com.porsche.hypercar.domain.configurator.model

import com.porsche.hypercar.presentation.configurator.EngineOption
import com.porsche.hypercar.presentation.configurator.ExteriorColor
import com.porsche.hypercar.presentation.configurator.InteriorOption
import com.porsche.hypercar.presentation.configurator.PriceLineItem
import com.porsche.hypercar.presentation.configurator.WheelOption

/**
 * Domain model representing a snapshot of the user's saved configuration.
 * This is independent of any persistence framework.
 */
data class SavedConfiguration(
    val id: Long = 0L,
    val carModel: String,
    val basePrice: Long,
    val selectedEngine: EngineOption,
    val selectedWheels: WheelOption,
    val selectedExteriorColor: ExteriorColor,
    val selectedInterior: InteriorOption,
    val isPerformancePackageSelected: Boolean,
    val totalPrice: Long,
    val optionsBreakdown: List<PriceLineItem>,
    val savedAtMillis: Long = System.currentTimeMillis()
)
