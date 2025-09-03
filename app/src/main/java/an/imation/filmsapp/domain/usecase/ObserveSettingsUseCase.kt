package an.imation.filmsapp.domain.usecase

import an.imation.filmsapp.domain.model.SettingsDomainModel
import an.imation.filmsapp.domain.repository.ISettingsRepository
import kotlinx.coroutines.flow.Flow

class ObserveSettingsUseCase(private val repo: ISettingsRepository) {
    operator fun invoke(): Flow<SettingsDomainModel> = repo.observeSettings()
}