package an.imation.filmsapp.presentation

import an.imation.filmsapp.presentation.screen.MovieScreen
import an.imation.filmsapp.presentation.screen.NavGraphs
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import an.imation.filmsapp.presentation.theme.FilmsAppTheme
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FilmsAppTheme {
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
}
