package com.porsche.hypercar.domain.configurator.model

data class PerformanceStats(
    val horsepower: Int,
    val topSpeedKmh: Int,
    val acceleration0To100: Double,
    val weightKg: Double // Đã sửa từ Int thành Double
)