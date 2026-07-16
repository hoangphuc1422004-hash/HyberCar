package com.porsche.hypercar.domain.configurator.usecase

import com.porsche.hypercar.presentation.configurator.ConfiguratorUiState
import com.porsche.hypercar.presentation.configurator.ConstraintWarning
import com.porsche.hypercar.presentation.configurator.PriceLineItem
import javax.inject.Inject

/**
 * Use case responsible for calculating the total price of the current configuration,
 * generating the price breakdown, and validating option constraints.
 *
 * This is a pure domain-layer class with no Android dependencies.
 */
class CalculateTotalPriceUseCase @Inject constructor() {

    companion object {
        /** Price for the Weissach Performance Package. */
        const val PERFORMANCE_PACKAGE_PRICE = 84_000L
    }

    /**
     * Calculates the total price and produces a detailed breakdown.
     *
     * @param state The current UI state with all selected options.
     * @return A [PriceCalculationResult] containing total price, line items, and constraint warnings.
     */
    operator fun invoke(state: ConfiguratorUiState): PriceCalculationResult {
        val lineItems = mutableListOf<PriceLineItem>()
        val warnings = mutableListOf<ConstraintWarning>()

        // ── Base price ───────────────────────────────────────────────────────
        lineItems.add(PriceLineItem(label = "Giá cơ bản – ${state.carModel}", price = state.basePrice))

        // ── Engine ───────────────────────────────────────────────────────────
        val engine = state.selectedEngine
        if (engine.additionalPrice > 0) {
            lineItems.add(PriceLineItem(label = "Động cơ: ${engine.displayName}", price = engine.additionalPrice))
        }
        // Constraint: Race engine requires Performance Package
        if (engine == com.porsche.hypercar.presentation.configurator.EngineOption.HYBRID_V8_RACE
            && !state.isPerformancePackageSelected
        ) {
            warnings.add(
                ConstraintWarning(
                    message = "Động cơ '${engine.displayName}' yêu cầu gói Performance (Weissach Package).",
                    affectedOption = "engine"
                )
            )
        }

        // ── Wheels ───────────────────────────────────────────────────────────
        val wheels = state.selectedWheels
        if (wheels.additionalPrice > 0) {
            lineItems.add(PriceLineItem(label = "Mâm: ${wheels.displayName}", price = wheels.additionalPrice))
        }
        // Constraint: Carbon wheels require Performance Package
        if (wheels.requiresPerformancePackage && !state.isPerformancePackageSelected) {
            warnings.add(
                ConstraintWarning(
                    message = "Mâm '${wheels.displayName}' chỉ đi kèm gói Performance (Weissach Package).",
                    affectedOption = "wheels"
                )
            )
        }

        // ── Exterior Color ──────────────────────────────────────────────────
        val color = state.selectedExteriorColor
        if (color.additionalPrice > 0) {
            lineItems.add(PriceLineItem(label = "Màu ngoại thất: ${color.displayName}", price = color.additionalPrice))
        }

        // ── Interior ─────────────────────────────────────────────────────────
        val interior = state.selectedInterior
        if (interior.additionalPrice > 0) {
            lineItems.add(PriceLineItem(label = "Nội thất: ${interior.displayName}", price = interior.additionalPrice))
        }

        // ── Performance Package ──────────────────────────────────────────────
        if (state.isPerformancePackageSelected) {
            lineItems.add(
                PriceLineItem(
                    label = "Weissach Performance Package",
                    price = PERFORMANCE_PACKAGE_PRICE
                )
            )
        }

        // ── Total ────────────────────────────────────────────────────────────
        val totalPrice = lineItems.sumOf { it.price }

        return PriceCalculationResult(
            totalPrice = totalPrice,
            lineItems = lineItems,
            constraintWarnings = warnings
        )
    }
}

/**
 * Immutable result of a price calculation.
 */
data class PriceCalculationResult(
    val totalPrice: Long,
    val lineItems: List<PriceLineItem>,
    val constraintWarnings: List<ConstraintWarning>
)
