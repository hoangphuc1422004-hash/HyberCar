package com.porsche.hypercar.domain.configurator.usecase

import com.porsche.hypercar.domain.configurator.model.SavedConfiguration
import com.porsche.hypercar.domain.configurator.repository.ConfigurationRepository
import javax.inject.Inject

/**
 * Use case responsible for loading a previously saved configuration
 * from local storage (Room database).
 */
class GetSavedConfigurationUseCase @Inject constructor(
    private val repository: ConfigurationRepository
) {
    /**
     * Retrieves the most recently saved configuration, or null if none exists.
     *
     * @return The saved configuration snapshot, or null.
     */
    suspend operator fun invoke(): SavedConfiguration? {
        return repository.getSavedConfiguration()
    }
}
