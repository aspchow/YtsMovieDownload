package com.avinash.ytsmoviedownload.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.avinash.ytsmoviedownload.ui.theme.YtsViewModel
import org.koin.androidx.compose.inject
import org.koin.androidx.compose.viewModel

@ExperimentalFoundationApi
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val viewModel : YtsViewModel by viewModel()

    NavHost(navController = navController, startDestination = NavConstant.Screen.MOVIES_LISTING) {

        composable(route = NavConstant.Screen.MOVIES_LISTING) {
            MoviesListingScreen(viewModel = viewModel, navController = navController)
        }

        composable(
            route = "${NavConstant.Screen.MOVIE_DETAILS}?${NavConstant.Args.Movie_ID}={${NavConstant.Args.Movie_ID}}",
            arguments = listOf(navArgument(NavConstant.Args.Movie_ID) {
                type = NavType.IntType
            })
        ) { backStack ->
            val movieId = backStack.arguments?.getInt(NavConstant.Args.Movie_ID) ?: -1
            MovieDetailsScreen(viewModel = viewModel)
        }
    }
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