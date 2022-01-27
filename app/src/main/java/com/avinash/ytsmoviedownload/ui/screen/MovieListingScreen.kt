package com.avinash.ytsmoviedownload.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import coil.compose.rememberImagePainter
import com.avinash.ytsmoviedownload.repository.ApiState
import com.avinash.ytsmoviedownload.repository.local.database.model.Movie
import com.avinash.ytsmoviedownload.ui.screen.destinations.MovieDetailsScreenDestination
import com.avinash.ytsmoviedownload.ui.theme.YtsViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.get
import org.koin.core.annotation.KoinInternalApi

@KoinInternalApi
@ExperimentalFoundationApi
@Destination(route = NavConstant.Screen.MOVIES_LISTING, start = true)
@Composable
fun MoviesListingScreen(navigator: DestinationsNavigator) {

    val viewmodel: YtsViewModel = get()

    val movies: List<Movie> by viewmodel.movies.collectAsState(initial = emptyList(),Dispatchers.IO)

    val searchContent by viewmodel.searchContent.collectAsState(Dispatchers.IO)

    val moviesSearchProgress: ApiState by viewmodel.moviesSearchProgress.collectAsState(Dispatchers.IO)

    Column {
        SearchBox(value = searchContent, searchState = moviesSearchProgress) { searchContent ->
            viewmodel.setSearch(searchContent = searchContent)
        }
        LazyVerticalGrid(cells = GridCells.Adaptive(154.dp), modifier = Modifier.animateContentSize()) {
            items(movies) { movie ->
                MovieItem(movie = movie) { selectedMovie ->
                    navigator.navigate(MovieDetailsScreenDestination(movie = selectedMovie))
                }
            }
        }
    }
}


@Composable
fun SearchBox(value: String, searchState: ApiState, onValueChange: (String) -> Unit) {

    Column(
        Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {

        Text(
            "Search & Find Movies, here..",
            style = MaterialTheme.typography.subtitle1.copy(fontSize = 16.sp),
            color = Color.White
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF211F30).copy(alpha = 0.5f))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row {
                Icon(
                    Icons.Outlined.Search,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                BasicTextField(
                    modifier = Modifier,
                    value = value,
                    onValueChange = onValueChange,
                    textStyle = MaterialTheme.typography.h2.copy(color = Color(0xFFBBBBBB))
                )

            }

            Row {
                if (searchState == ApiState.InProgress)
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )

                if (value.isNotEmpty()) {

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        Icons.Outlined.Clear,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                onValueChange("")
                            })
                }
            }
        }
    }
}


@Composable
fun MovieItem(movie: Movie, onClick: (Movie) -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable {
                onClick(movie)
            }
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(width = 154.dp, height = 184.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0f),
                            Color.White.copy(alpha = 0.3f)
                        )
                    )
                ),
            painter = rememberImagePainter(movie.largeCoverImage),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = movie.titleLong,
            style = MaterialTheme.typography.subtitle2,
            textAlign = TextAlign.Center,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(4.dp))
    }
}