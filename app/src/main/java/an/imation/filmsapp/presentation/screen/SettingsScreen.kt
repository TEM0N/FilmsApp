package an.imation.filmsapp.presentation.screen

import an.imation.filmsapp.R
import an.imation.filmsapp.domain.model.Language
import an.imation.filmsapp.presentation.setting.SettingsEvent
import an.imation.filmsapp.presentation.setting.SettingsIntent
import an.imation.filmsapp.presentation.setting.SettingsState
import an.imation.filmsapp.presentation.vm.SettingsViewModel
import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun SettingsScreen(navigator: DestinationsNavigator) {
    val vm = koinViewModel<SettingsViewModel>()
    val state: SettingsState by vm.uiState.collectAsStateWithLifecycle()
    val intent: (SettingsIntent) -> Unit by remember { mutableStateOf(vm::onIntent) }
    val event: Flow<SettingsEvent> by remember { mutableStateOf(vm.events) }
    val context = LocalContext.current

    UI(
        state = state,
        intent = intent,
        navigator = navigator
    )

    LaunchedEffect(Unit) {
        event.collect { e ->
            when (e) {
                is SettingsEvent.ShowError -> {
                    Toast.makeText(context, context.getString(e.resId), Toast.LENGTH_SHORT).show()
                }
                is SettingsEvent.RecreateForLanguage -> {
                    (context as? Activity)?.recreate()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun UI(
    state: SettingsState = SettingsState(),
    intent: (SettingsIntent) -> Unit = {},
    navigator: DestinationsNavigator? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(
            title = { Text(stringResource(R.string.settings)) },
            navigationIcon = {
                IconButton(onClick = { navigator?.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
            },
            windowInsets = WindowInsets(0, 0, 0, 0)
        )

        Column(modifier = Modifier.padding(vertical = 16.dp, horizontal = 26.dp)) {
            /*ThemeBlock(
                isDarkTheme = state.isDarkTheme,
                intent = intent
            )*/
            Spacer(Modifier.height(24.dp))
            LanguageBlock(
                current = state.language,
                intent = intent
            )
        }
    }
}


@Composable
@Preview
private fun LanguageBlock(
    current: Language = Language.EN,
    intent: (SettingsIntent) -> Unit = {}
) {
    Column {
        Text(stringResource(R.string.language))
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = current == Language.EN,
                onClick = { intent(SettingsIntent.ChangeLanguage(Language.EN)) }
            )
            Text(stringResource(R.string.english))
            Spacer(Modifier.width(16.dp))
            RadioButton(
                selected = current == Language.RU,
                onClick = { intent(SettingsIntent.ChangeLanguage(Language.RU)) }
            )
            Text(stringResource(R.string.russian))
        }
    }
}
