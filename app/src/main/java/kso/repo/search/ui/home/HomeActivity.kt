package kso.repo.search.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kso.repo.search.app.AppNavHost
import kso.repo.search.ui.theme.RepoSearchTheme

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RepoSearchTheme {
                val scaffoldState = rememberScaffoldState()
                val navController = rememberNavController()
                RepoSearchTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(color = MaterialTheme.colors.background) {
                        AppNavHost(navController, scaffoldState )
                    }
                }
            }
        }
    }

}



