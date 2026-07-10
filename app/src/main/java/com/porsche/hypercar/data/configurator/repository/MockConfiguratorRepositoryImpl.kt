package com.porsche.hypercar.data.configurator.repository

import com.porsche.hypercar.domain.common.Resource
import com.porsche.hypercar.domain.configurator.model.BrakeMaterial
import com.porsche.hypercar.domain.configurator.model.CarConfiguration
import com.porsche.hypercar.domain.configurator.model.ExteriorOption
import com.porsche.hypercar.domain.configurator.model.InteriorOption
import com.porsche.hypercar.domain.configurator.model.PerformanceStats
import com.porsche.hypercar.domain.configurator.repository.ConfiguratorRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MockConfiguratorRepositoryImpl @Inject constructor() : ConfiguratorRepository {

    override fun getAvailableColors(): Flow<Resource<List<ExteriorOption.PaintColor>>> = flow {
        emit(Resource.Loading())
        delay(400)
        emit(Resource.Success(
            listOf(
                ExteriorOption.PaintColor("color_1", "Carbon Black", 0.0, null, "#141313"),
                ExteriorOption.PaintColor("color_2", "Racing Crimson", 4200.0, null, "#D00000"),
                ExteriorOption.PaintColor("color_3", "Electric Velocity Blue", 4200.0, null, "#00A3FF"),
                ExteriorOption.PaintColor("color_4", "Titanium Silver", 3500.0, null, "#C9C6C5")
            )
        ))
    }

    override fun getAvailableWheels(): Flow<Resource<List<ExteriorOption.WheelType>>> = flow {
        emit(Resource.Loading())
        delay(300)
        emit(Resource.Success(
            listOf(
                ExteriorOption.WheelType("wheel_1", "20/21-inch GT3 RS Aluminum", 0.0, null, 0.0),
                ExteriorOption.WheelType("wheel_2", "20/21-inch GT3 RS Forged Magnesium", 16400.0, null, 11.5),
                ExteriorOption.WheelType("wheel_3", "20/21-inch GT3 RS Forged", 12500.0, null, 5.0)
            )
        ))
    }

    override fun getAvailableBrakes(): Flow<Resource<List<ExteriorOption.BrakeSystem>>> = flow {
        emit(Resource.Loading())
        delay(300)
        emit(Resource.Success(
            listOf(
                ExteriorOption.BrakeSystem("brake_1", "Porsche Surface Coated Brake (PSCB)", 0.0, null, BrakeMaterial.STEEL),
                ExteriorOption.BrakeSystem("brake_2", "Porsche Ceramic Composite Brake (PCCB)", 10100.0, null, BrakeMaterial.CARBON_CERAMIC)
            )
        ))
    }

    override fun getAvailableInteriors(): Flow<Resource<List<InteriorOption>>> = flow {
        emit(Resource.Loading())
        delay(300)
        emit(Resource.Success(
            listOf(
                InteriorOption("int_1", InteriorOption.MaterialType.LEATHER, "Standard Black", InteriorOption.TrimType.MATTE_ALUMINUM, 0.0),
                InteriorOption("int_2", InteriorOption.MaterialType.RACE_TEX, "GTS Carmine Red", InteriorOption.TrimType.CARBON_FIBER, 4500.0),
                InteriorOption("int_3", InteriorOption.MaterialType.CLUB_LEATHER, "Truffle Brown", InteriorOption.TrimType.WOOD, 6200.0)
            )
        ))
    }

    override suspend fun getBasePerformanceStats(): Resource<PerformanceStats> {
        delay(200)
        return Resource.Success(
            PerformanceStats(horsepower = 518, topSpeedKmh = 296, acceleration0To100 = 3.2, weightKg = 1450.0)
        )
    }

    override suspend fun saveConfiguration(config: CarConfiguration): Resource<Unit> {
        delay(400)
        return Resource.Success(Unit)
    }

    override suspend fun getSavedConfiguration(id: String): CarConfiguration? {
        return null
    }
}