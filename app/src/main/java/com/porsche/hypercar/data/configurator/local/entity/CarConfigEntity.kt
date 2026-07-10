package com.porsche.hypercar.data.configurator.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car_configurations")
data class CarConfigEntity(
    @PrimaryKey
    val id: String = "current_config",
    val exteriorColorId: String,
    val wheelTypeId: String,
    val brakeSystemId: String,
    val interiorId: String,
    val totalPrice: Double
)