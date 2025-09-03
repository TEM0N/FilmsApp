package an.imation.filmsapp.presentation.setting

import an.imation.filmsapp.domain.model.Language

sealed interface SettingsIntent {
    //data class ChangeTheme(val isDark: Boolean) : SettingsIntent
    data class ChangeLanguage(val language: Language) : SettingsIntent
    data object LoadSettings : SettingsIntent
}