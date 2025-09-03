package an.imation.filmsapp.presentation

import an.imation.filmsapp.R
import an.imation.filmsapp.domain.model.Language
import an.imation.filmsapp.domain.model.SettingsDomainModel
import an.imation.filmsapp.domain.usecase.ObserveSettingsUseCase
import an.imation.filmsapp.presentation.screen.NavGraphs
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import an.imation.filmsapp.presentation.theme.FilmsAppTheme
import android.content.Context
import android.content.res.Configuration
import android.preference.PreferenceManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import org.koin.androidx.compose.get
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(newBase)
        val lang = prefs.getString("language", Language.EN.name)
        val locale = when (Language.valueOf(lang ?: Language.EN.name)) {
            Language.RU -> Locale("ru")
            Language.EN -> Locale("en")
        }
        val config = Configuration(newBase.resources.configuration).apply {
            setLocale(locale)
        }
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val observeSettings: ObserveSettingsUseCase = get()
            val settings by observeSettings().collectAsState(
                initial = SettingsDomainModel(isDarkTheme = false, language = Language.EN)
            )

            FilmsAppTheme(darkTheme = settings.isDarkTheme) {
                Scaffold { padd ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padd)
                    ) {
                        val navController = rememberNavController()

                        DestinationsNavHost(
                            navGraph = NavGraphs.root,
                            navController = navController
                        )
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

}