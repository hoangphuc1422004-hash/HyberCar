package com.porsche.hypercar.domain.configurator.usecase

import com.porsche.hypercar.domain.configurator.model.SavedConfiguration
import com.porsche.hypercar.domain.configurator.repository.ConfigurationRepository
import javax.inject.Inject

/**
 * Use case responsible for persisting the current car configuration
 * to local storage (Room database).
 */
class SaveConfigurationUseCase @Inject constructor(
    private val repository: ConfigurationRepository
) {
    /**
     * Saves the given configuration.
     *
     * @param configuration The configuration snapshot to persist.
     * @throws Exception if the save operation fails.
     */
    suspend operator fun invoke(configuration: SavedConfiguration) {
        repository.saveConfiguration(configuration)
    }
}
