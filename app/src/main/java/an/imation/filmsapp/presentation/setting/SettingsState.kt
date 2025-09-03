package an.imation.filmsapp.presentation.setting

import an.imation.filmsapp.domain.model.Language

data class SettingsState(
    val isDarkTheme: Boolean = false,
    val language: Language = Language.EN,
    val error: Int? = null,
    val isLoading: Boolean = false
)