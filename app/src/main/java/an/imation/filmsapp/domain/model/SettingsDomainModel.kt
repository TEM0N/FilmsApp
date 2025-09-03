package an.imation.filmsapp.domain.model

data class SettingsDomainModel(
    val isDarkTheme: Boolean,
    val language: Language
)

enum class Language { RU, EN }
