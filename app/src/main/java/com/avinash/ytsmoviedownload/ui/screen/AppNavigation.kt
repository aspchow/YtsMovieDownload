package com.avinash.ytsmoviedownload.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.avinash.ytsmoviedownload.ui.theme.YtsViewModel
import com.ramcosta.composedestinations.DestinationsNavHost
import org.koin.androidx.compose.inject
import org.koin.androidx.compose.viewModel
import org.koin.core.annotation.KoinInternalApi

@KoinInternalApi
@ExperimentalFoundationApi
@Composable
fun AppNavigation() {
    DestinationsNavHost(navGraph = NavGraphs.root)
}


object NavConstant {


    object Screen {
        const val MOVIES_LISTING = "movies_listing"
        const val MOVIE_DETAILS = "movies_details"

    }


    object Args {
        const val Movie_ID = "movieId"
    }
}