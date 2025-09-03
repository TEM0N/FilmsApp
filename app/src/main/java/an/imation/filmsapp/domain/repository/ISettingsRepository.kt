package an.imation.filmsapp.domain.repository

import an.imation.filmsapp.domain.TResult
import an.imation.filmsapp.domain.model.Language
import an.imation.filmsapp.domain.model.MovieExceptionDomainModel
import an.imation.filmsapp.domain.model.SettingsDomainModel
import kotlinx.coroutines.flow.Flow

interface ISettingsRepository {
    fun observeSettings(): Flow<SettingsDomainModel>
    suspend fun setTheme(isDark: Boolean): TResult<Unit, MovieExceptionDomainModel>
    suspend fun setLanguage(language: Language): TResult<Unit, MovieExceptionDomainModel>
}

