package com.avinash.ytsmoviedownload.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.avinash.ytsmoviedownload.ui.theme.YtsViewModel

@Composable
fun MovieDetailsScreen(viewModel: YtsViewModel) {



    val movie = viewModel.getSelectedMovie()

    val torrents = viewModel.getTorrents(movieId = movie.id)

    val scrollableState = rememberScrollState()

    Column(
        modifier = Modifier.verticalScroll(scrollableState)
    ) {
        Image(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomEnd = 30.dp, bottomStart = 30.dp)),
            painter = rememberImagePainter(movie.largeCoverImage),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )

        Column(
            Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {

            Text(
                text = movie.title,
                style = MaterialTheme.typography.h3.copy(fontSize = 24.sp),
                color = Color.White
            )

        }
    }
}