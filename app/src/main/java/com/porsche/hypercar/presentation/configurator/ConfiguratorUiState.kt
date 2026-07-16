package com.porsche.hypercar.presentation.configurator

/**
 * Represents the complete UI state for the Configurator screen.
 * This is an immutable data class following UDF (Unidirectional Data Flow) principles.
 * The ViewModel is the single source of truth for this state.
 */
data class ConfiguratorUiState(
    val carModel: String = "Porsche 918 Spyder",
    val basePrice: Long = 845_000L,

    // Engine options
    val selectedEngine: EngineOption = EngineOption.HYBRID_V8,
    val availableEngines: List<EngineOption> = EngineOption.entries,

    // Wheel options
    val selectedWheels: WheelOption = WheelOption.STANDARD_ALLOY,
    val availableWheels: List<WheelOption> = WheelOption.entries,

    // Exterior color
    val selectedExteriorColor: ExteriorColor = ExteriorColor.GT_SILVER,
    val availableExteriorColors: List<ExteriorColor> = ExteriorColor.entries,

    // Interior
    val selectedInterior: InteriorOption = InteriorOption.STANDARD_LEATHER,
    val availableInteriors: List<InteriorOption> = InteriorOption.entries,

    // Performance package
    val isPerformancePackageSelected: Boolean = false,

    // Calculated pricing
    val totalPrice: Long = basePrice,
    val optionsBreakdown: List<PriceLineItem> = emptyList(),

    // Validation / constraints
    val constraintWarnings: List<ConstraintWarning> = emptyList(),

    // Loading / error states
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null
)

// ─── Enums for configurable options ───────────────────────────────────────────

/**
 * Available engine upgrades.
 */
enum class EngineOption(
    val displayName: String,
    val description: String,
    val additionalPrice: Long
) {
    HYBRID_V8(
        displayName = "4.6L V8 Hybrid",
        description = "Tiêu chuẩn – 608 HP kết hợp động cơ điện",
        additionalPrice = 0L
    ),
    HYBRID_V8_SPORT(
        displayName = "4.6L V8 Hybrid Sport",
        description = "Nâng cấp phần mềm – 652 HP, phản hồi ga nhanh hơn",
        additionalPrice = 32_000L
    ),
    HYBRID_V8_RACE(
        displayName = "4.6L V8 Hybrid Race",
        description = "Cấu hình đường đua – 710 HP, chỉ đi kèm gói Performance",
        additionalPrice = 75_000L
    );
}

/**
 * Available wheel configurations.
 */
enum class WheelOption(
    val displayName: String,
    val description: String,
    val additionalPrice: Long,
    val requiresPerformancePackage: Boolean = false
) {
    STANDARD_ALLOY(
        displayName = "20\" Hợp kim tiêu chuẩn",
        description = "Mâm hợp kim nhẹ sơn bạc",
        additionalPrice = 0L
    ),
    SPORT_ALLOY(
        displayName = "20\" Hợp kim Sport",
        description = "Mâm hợp kim đen bóng thiết kế thể thao",
        additionalPrice = 8_500L
    ),
    CARBON_FIBER(
        displayName = "20\" Sợi carbon",
        description = "Mâm carbon siêu nhẹ – chỉ đi kèm gói Performance",
        additionalPrice = 18_000L,
        requiresPerformancePackage = true
    );
}

/**
 * Available exterior color options.
 */
enum class ExteriorColor(
    val displayName: String,
    val hexCode: String,
    val additionalPrice: Long
) {
    GT_SILVER(
        displayName = "GT Silver Metallic",
        hexCode = "#B0B0B0",
        additionalPrice = 0L
    ),
    RACING_YELLOW(
        displayName = "Racing Yellow",
        hexCode = "#FFD700",
        additionalPrice = 4_200L
    ),
    GUARDS_RED(
        displayName = "Guards Red",
        hexCode = "#C41E3A",
        additionalPrice = 4_200L
    ),
    LIQUID_METAL_BLUE(
        displayName = "Liquid Metal Blue",
        hexCode = "#1B3A5C",
        additionalPrice = 12_600L
    ),
    ACID_GREEN(
        displayName = "Acid Green",
        hexCode = "#A8D600",
        additionalPrice = 12_600L
    );
}

/**
 * Available interior options.
 */
enum class InteriorOption(
    val displayName: String,
    val description: String,
    val additionalPrice: Long
) {
    STANDARD_LEATHER(
        displayName = "Da tiêu chuẩn – Đen",
        description = "Nội thất da đen Nappa cơ bản",
        additionalPrice = 0L
    ),
    PREMIUM_LEATHER(
        displayName = "Da Nappa cao cấp – Hai tông màu",
        description = "Nội thất da Nappa hai tông màu với chỉ khâu tương phản",
        additionalPrice = 9_800L
    ),
    ALCANTARA_RACE(
        displayName = "Alcantara Race",
        description = "Nội thất Alcantara phong cách đường đua, sợi carbon",
        additionalPrice = 15_500L
    );
}

// ─── Supporting data classes ──────────────────────────────────────────────────

/**
 * Represents a single line item in the price breakdown.
 */
data class PriceLineItem(
    val label: String,
    val price: Long
)

/**
 * Represents a constraint warning when the user selects an incompatible option.
 */
data class ConstraintWarning(
    val message: String,
    val affectedOption: String
)
