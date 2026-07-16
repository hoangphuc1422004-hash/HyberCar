package com.porsche.hypercar.domain.configurator.repository

import com.porsche.hypercar.domain.configurator.model.SavedConfiguration

/**
 * Repository contract for configuration persistence.
 * The domain layer defines this interface; the data layer provides the implementation.
 */
interface ConfigurationRepository {

    /** Persist the given configuration. */
    suspend fun saveConfiguration(configuration: SavedConfiguration)

    /** Retrieve the most recently saved configuration, or null if none exists. */
    suspend fun getSavedConfiguration(): SavedConfiguration?
}
