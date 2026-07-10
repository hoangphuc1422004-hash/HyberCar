package com.porsche.hypercar.domain.configurator.model

data class InteriorOption(
    val id: String,
    val material: MaterialType,
    val colorName: String,
    val trim: TrimType,
    val price: Double
) {
    enum class MaterialType { LEATHER, RACE_TEX, CLUB_LEATHER }
    enum class TrimType { CARBON_FIBER, MATTE_ALUMINUM, WOOD }

    companion object {
        fun default() = InteriorOption(
            id = "default",
            material = MaterialType.LEATHER,
            colorName = "Standard Black",
            trim = TrimType.MATTE_ALUMINUM,
            price = 0.0
        )
    }
}