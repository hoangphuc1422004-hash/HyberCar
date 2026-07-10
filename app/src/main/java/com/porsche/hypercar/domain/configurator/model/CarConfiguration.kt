package com.porsche.hypercar.domain.configurator.model

data class CarConfiguration(
    val id: String = "",
    val basePrice: Double = 0.0,
    val selectedColor: ExteriorOption.PaintColor,
    val selectedWheel: ExteriorOption.WheelType,
    val selectedBrake: ExteriorOption.BrakeSystem,
    val selectedInterior: InteriorOption?,
    val hasWeissachPackage: Boolean = false,
    val weissachPackagePrice: Double = 42000.0,
    val baseStats: PerformanceStats,
    val totalPrice: Double = 0.0
) {
    companion object {
        // Hàm hỗ trợ UI khi chưa có dữ liệu mạng (Sử dụng tham số định danh để tuyệt đối an toàn)
        fun empty() = CarConfiguration(
            selectedColor = ExteriorOption.PaintColor(id = "", name = "", price = 0.0, imageUrl = null, hexCode = "#000000"),
            selectedWheel = ExteriorOption.WheelType(id = "", name = "", price = 0.0, imageUrl = null, weightReductionKg = 0.0),
            selectedBrake = ExteriorOption.BrakeSystem(id = "", name = "", price = 0.0, imageUrl = null, material = BrakeMaterial.STEEL),
            selectedInterior = null,
            baseStats = PerformanceStats(horsepower = 0, topSpeedKmh = 0, acceleration0To100 = 0.0, weightKg = 0.0)
        )
    }
}