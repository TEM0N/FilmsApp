package an.imation.filmsapp.data.repositoryimpl

import an.imation.filmsapp.domain.TResult
import an.imation.filmsapp.domain.model.Language
import an.imation.filmsapp.domain.model.MovieExceptionDomainModel
import an.imation.filmsapp.domain.model.SettingsDomainModel
import an.imation.filmsapp.domain.repository.ISettingsRepository
import android.content.Context
import android.preference.PreferenceManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.*

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val context: Context
) : ISettingsRepository {

    companion object {
        private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
        private val LANGUAGE_KEY   = stringPreferencesKey("language")
        private const val SP_LANGUAGE = "language"
    }

    override fun observeSettings(): Flow<SettingsDomainModel> =
        dataStore.data
            .map { prefs ->
                val isDark = prefs[DARK_THEME_KEY] ?: false
                val lang = prefs[LANGUAGE_KEY]?.let { Language.valueOf(it) } ?: Language.EN
                SettingsDomainModel(isDark, lang)
            }
            .distinctUntilChanged()

    override suspend fun setTheme(isDark: Boolean): TResult<Unit, MovieExceptionDomainModel> =
        try {
            dataStore.edit { it[DARK_THEME_KEY] = isDark }
            TResult.Success(Unit)
        } catch (e: Throwable) {
            TResult.Error(MovieExceptionDomainModel.Other(e))
        }

    override suspend fun setLanguage(language: Language): TResult<Unit, MovieExceptionDomainModel> =
        try {
            dataStore.edit { it[LANGUAGE_KEY] = language.name }
            PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(SP_LANGUAGE, language.name)
                .apply()
            TResult.Success(Unit)
        } catch (e: Throwable) {
            TResult.Error(MovieExceptionDomainModel.Other(e))
        }
}
