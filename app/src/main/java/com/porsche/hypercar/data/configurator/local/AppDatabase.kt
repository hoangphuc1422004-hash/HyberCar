package com.porsche.hypercar.data.configurator.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.porsche.hypercar.data.configurator.local.dao.ConfiguratorDao
import com.porsche.hypercar.data.configurator.local.entity.CarConfigEntity

@Database(entities = [CarConfigEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun configuratorDao(): ConfiguratorDao
}