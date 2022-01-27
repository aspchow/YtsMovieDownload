package com.avinash.ytsmoviedownload.ui.screen

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.avinash.ytsmoviedownload.R.drawable
import com.avinash.ytsmoviedownload.repository.local.database.model.Movie
import com.avinash.ytsmoviedownload.repository.local.database.model.file
import com.avinash.ytsmoviedownload.repository.remote.DownloadState
import com.avinash.ytsmoviedownload.ui.theme.YtsViewModel
import com.avinash.ytsmoviedownload.utils.openFile
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.get
import org.koin.core.annotation.KoinInternalApi


@KoinInternalApi
@ExperimentalFoundationApi
@Destination(route = NavConstant.Screen.MOVIE_DETAILS)
@Composable
fun MovieDetailsScreen(movie: Movie) {

    val viewModel: YtsViewModel = get()

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val torrents by viewModel.getTorrents(movieId = movie.id).collectAsState(initial = emptyList(),Dispatchers.IO)

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
                .padding(24.dp),
        ) {

            TitleWithLanguage(movie)

            Spacer(modifier = Modifier.height(12.dp))

            RuntimeAndRating(movie)

            Divider()

            ReleaseYearAndGenre(movie)

            Divider()

            Column {

                HeaderText(text = "Torrents (${torrents.size})")

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ) {
                    torrents.forEach { torrent ->

                        var downloadState by remember { mutableStateOf(if (torrent.file.exists()) DownloadState.Success else DownloadState.Pending) }

                        TorrentChips(
                            quality = torrent.quality,
                            size = torrent.size,
                            downloadState = downloadState
                        ) {

                            if (torrent.file.exists()) {
                                openFile(torrent.file)
                                return@TorrentChips
                            }
                            downloadState = DownloadState.Progress(10)
                            coroutineScope.launch(Dispatchers.IO) {
                                viewModel.downloadFile(torrent.file, torrent.url).collect {
                                    withContext(Dispatchers.Main) {
                                        when (it) {
                                            is DownloadState.Success -> {
                                                downloadState = DownloadState.Success
                                                Toast.makeText(
                                                    context,
                                                    "Download Success",
                                                    Toast.LENGTH_SHORT
                                                )
                                                    .show()
                                            }
                                            is DownloadState.Error -> {
                                                Toast.makeText(
                                                    context,
                                                    "Download Failed",
                                                    Toast.LENGTH_SHORT
                                                )
                                                    .show()
                                                downloadState = DownloadState.Pending
                                            }
                                            is DownloadState.Progress -> {


                                            }
                                        }
                                    }
                                }
                            }

                        }
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                }

            }

            Divider()

            Synopsis(movie)
        }
    }
}


@Composable
fun TorrentChips(quality: String, size: String, downloadState: DownloadState, onClick: () -> Unit) {

    Row(
        modifier = Modifier
            .clickable {
                onClick()
            }
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.25f),
                        Color.White.copy(alpha = 0.05f)
                    )
                ),
                shape = RoundedCornerShape(30.dp)
            )
            .clip(RoundedCornerShape(30.dp))
            .padding(horizontal = 15.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "$quality ($size)",
            style = MaterialTheme.typography.subtitle1,
            color = Color.White
        )

        Spacer(modifier = Modifier.width(10.dp))

        Icon(
            modifier = Modifier.height(20.dp),
            painter = painterResource(id = if (downloadState == DownloadState.Success) drawable.download_completed else if (downloadState == DownloadState.Pending) drawable.download_torrent else drawable.downloading_in_progress),
            contentDescription = null,
            tint = Color.White
        )
    }
}

@Composable
private fun Synopsis(movie: Movie) {
    Column {

        HeaderText(text = "Synopsis")

        Spacer(modifier = Modifier.height(12.dp))

        SubtitleText(text = movie.synopsis)

    }
}

@Composable
private fun ReleaseYearAndGenre(movie: Movie) {
    Row {

        Column {
            HeaderText(text = "Release Year")

            Spacer(modifier = Modifier.height(12.dp))

            SubtitleText(text = movie.year.toString())
        }

        Spacer(modifier = Modifier.width(50.dp))

        Column(Modifier.fillMaxWidth()) {
            HeaderText(text = "Genre")

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                movie.genres.forEach { genre ->
                    TextChip(text = genre, cornerRadius = 30.dp)
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }
        }

    }
}

@Composable
fun HeaderText(text: String) {
    Text(
        text = text, style = MaterialTheme.typography.h3.copy(
            fontSize = 17.sp,
            fontWeight = FontWeight.W300
        ), color = Color.White
    )
}


@Composable
private fun Divider() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
            .height(0.22.dp)
            .background(Color(0xFF515151))
    )
}

@Composable
private fun RuntimeAndRating(movie: Movie) {
    Row(Modifier.fillMaxWidth()) {
        IconTextDescription(
            drawableResource = drawable.outline_watch_later_24,
            text = "${movie.runtime} minutes"
        )

        Spacer(modifier = Modifier.width(20.dp))

        IconTextDescription(
            drawableResource = drawable.outline_star_24,
            text = "${movie.rating} (IMDb)"
        )

    }
}

@Composable
private fun TitleWithLanguage(movie: Movie) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = movie.title,
            style = MaterialTheme.typography.h3.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.W300
            ),
            color = Color.White
        )

        Spacer(modifier = Modifier.width(8.dp))

        TextChip(movie.language)

    }
}

@Composable
private fun TextChip(text: String, cornerRadius: Dp = 5.dp) {
    Text(
        modifier = Modifier
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.25f),
                        Color.White.copy(alpha = 0.05f)
                    )
                ),
                shape = RoundedCornerShape(cornerRadius)
            )
            .clip(RoundedCornerShape(cornerRadius))
            .padding(horizontal = 7.dp, vertical = 3.dp),
        text = text,
        style = MaterialTheme.typography.subtitle1,
        color = Color.White
    )
}


@Composable
fun IconTextDescription(@DrawableRes drawableResource: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            modifier = Modifier.size(13.dp),
            painter = painterResource(id = drawableResource),
            contentDescription = null,
            tint = Color(0xFFBCBCBC)
        )
        Spacer(modifier = Modifier.width(8.dp))
        SubtitleText(text)
    }
}

@Composable
private fun SubtitleText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.subtitle1.copy(fontSize = 13.sp),
        color = Color(0xFFBCBCBC)
    )
}