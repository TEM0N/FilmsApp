package an.imation.filmsapp.presentation.vm

import an.imation.filmsapp.R
import an.imation.filmsapp.domain.TResult
import an.imation.filmsapp.domain.model.Language
import an.imation.filmsapp.domain.usecase.ObserveSettingsUseCase
import an.imation.filmsapp.domain.usecase.SetLanguageUseCase
import an.imation.filmsapp.domain.usecase.SetThemeUseCase
import an.imation.filmsapp.presentation.setting.SettingsEvent
import an.imation.filmsapp.presentation.setting.SettingsIntent
import an.imation.filmsapp.presentation.setting.SettingsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import an.imation.filmsapp.presentation.parseToString
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart


class SettingsViewModel(
    private val setTheme: SetThemeUseCase,
    private val setLanguage: SetLanguageUseCase,
    private val observeSettings: ObserveSettingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsState())
    val uiState: StateFlow<SettingsState> = _uiState.asStateFlow()

    private val _events = SingleFlowEvent<SettingsEvent>(viewModelScope)
    val events = _events.flow

    init {
        viewModelScope.launch {
            observeSettings()
                .onStart { _uiState.update { it.copy(isLoading = true, error = null) } }
                .catch {
                    _uiState.update { it.copy(isLoading = false, error = R.string.error_loading) }
                    _events.emit(SettingsEvent.ShowError(R.string.error_loading))
                }
                .collect { settings ->
                    _uiState.update {
                        it.copy(
                            isDarkTheme = settings.isDarkTheme,
                            language = settings.language,
                            isLoading = false,
                            error = null
                        )
                    }
                }
        }
    }

    fun onIntent(intent: SettingsIntent) = when (intent) {
        is SettingsIntent.ChangeTheme    -> changeTheme(intent.isDark)
        is SettingsIntent.ChangeLanguage -> changeLanguage(intent.language)
        SettingsIntent.LoadSettings      -> Unit
    }

    private fun changeTheme(isDark: Boolean) {
        viewModelScope.launch {
            when (val result = setTheme(isDark)) {
                is TResult.Success -> Unit
                is TResult.Error   -> _events.emit(SettingsEvent.ShowError(result.exception.parseToString()))
            }
        }
    }

    private fun changeLanguage(language: Language) {
        viewModelScope.launch {
            when (val result = setLanguage(language)) {
                is TResult.Success -> {
                    _events.emit(SettingsEvent.RecreateForLanguage(language))
                }
                is TResult.Error -> _events.emit(SettingsEvent.ShowError(result.exception.parseToString()))
            }
        }
    }
}
