package an.imation.filmsapp.domain.usecase

import an.imation.filmsapp.domain.TResult
import an.imation.filmsapp.domain.model.Language
import an.imation.filmsapp.domain.model.MovieExceptionDomainModel
import an.imation.filmsapp.domain.repository.ISettingsRepository

class SetLanguageUseCase(private val repo: ISettingsRepository) {
    suspend operator fun invoke(language: Language): TResult<Unit, MovieExceptionDomainModel> = repo.setLanguage(language)
}