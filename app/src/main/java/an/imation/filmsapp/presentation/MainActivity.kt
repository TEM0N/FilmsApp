package an.imation.filmsapp.presentation

import an.imation.filmsapp.presentation.screen.MovieScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import an.imation.filmsapp.presentation.theme.FilmsAppTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier

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
                        MovieScreen()
                    }
                }
            }
        }
    }
}
