package an.imation.filmsapp.presentation.setting

import an.imation.filmsapp.domain.model.Language

sealed interface SettingsEvent {
    data class ShowError(val resId: Int) : SettingsEvent
    data class RecreateForLanguage(val language: Language) : SettingsEvent
}
