package com.porsche.hypercar.domain.configurator.model

// Khai báo các Option có thể chọn cho xe
sealed class ExteriorOption {
    abstract val id: String
    abstract val name: String
    abstract val price: Double // Đã sửa từ Long thành Double
    abstract val imageUrl: String?

    data class PaintColor(
        override val id: String,
        override val name: String,
        override val price: Double,
        override val imageUrl: String?,
        val hexCode: String
    ) : ExteriorOption()

    data class WheelType(
        override val id: String,
        override val name: String,
        override val price: Double,
        override val imageUrl: String?,
        val weightReductionKg: Double
    ) : ExteriorOption()

    data class BrakeSystem(
        override val id: String,
        override val name: String,
        override val price: Double,
        override val imageUrl: String?,
        val material: BrakeMaterial
    ) : ExteriorOption()
}

enum class BrakeMaterial {
    STEEL, CARBON_CERAMIC
}