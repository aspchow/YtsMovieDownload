package com.avinash.ytsmoviedownload

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.avinash.ytsmoviedownload.repository.ApiState
import com.avinash.ytsmoviedownload.repository.Repository
import com.avinash.ytsmoviedownload.ui.screen.AppNavigation
import com.avinash.ytsmoviedownload.ui.theme.YtsMovieDownloadTheme
import com.avinash.ytsmoviedownload.utils.globalActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.annotation.KoinInternalApi

class MainActivity : ComponentActivity() {

    private val repository: Repository by inject()

    @KoinInternalApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        globalActivity = this
        setContent {

            val coroutineScope = rememberCoroutineScope { Dispatchers.IO }

            coroutineScope.launch {
                repository.getMoviesFromServer().collect { apiState ->
                    when (apiState) {
                        is ApiState.Failure -> {
                            print(apiState.exception)
                        }
                        ApiState.Success -> {

                        }
                    }
                }
            }

            YtsMovieDownloadTheme {

                Surface(
                    color = Color(0xFF15141F),
                    modifier = Modifier.fillMaxSize()
                ) {
                    AppNavigation()
                }
            }
        }
    }


}


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    YtsMovieDownloadTheme {
        Greeting("Android")
    }
}