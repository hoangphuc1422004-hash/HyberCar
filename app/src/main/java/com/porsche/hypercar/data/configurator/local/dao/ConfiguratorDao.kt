package com.porsche.hypercar.data.configurator.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.porsche.hypercar.data.configurator.local.entity.CarConfigEntity

@Dao
interface ConfiguratorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfiguration(config: CarConfigEntity)

    @Query("SELECT * FROM car_configurations WHERE id = :id")
    suspend fun getConfigurationById(id: String): CarConfigEntity?
}