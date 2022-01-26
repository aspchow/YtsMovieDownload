package com.avinash.ytsmoviedownload.repository

import androidx.room.PrimaryKey
import com.avinash.ytsmoviedownload.repository.local.database.model.Movie
import com.avinash.ytsmoviedownload.repository.local.database.model.Torrent
import com.avinash.ytsmoviedownload.repository.remote.model.MovieFromApi

class MovieMapper {
    fun mapMoviesFromResponse(moviesFromApi: List<MovieFromApi>): Pair<List<Movie>, List<Torrent>> {
        val movies = mutableListOf<Movie>()
        val torrents = mutableListOf<Torrent>()

        moviesFromApi.forEach { movieFromApi ->
            movies.add(
                Movie(
                    id = movieFromApi.id,
                    backgroundImage = movieFromApi.background_image,
                    backgroundImageOriginal = movieFromApi.background_image_original,
                    dateUploaded = movieFromApi.date_uploaded,
                    dateUploadedUnix = movieFromApi.date_uploaded_unix,
                    descriptionFull = movieFromApi.description_full,
                    genres = movieFromApi.genres,
                    imdbCode = movieFromApi.imdb_code,
                    language = movieFromApi.language,
                    largeCoverImage = movieFromApi.large_cover_image,
                    mediumCoverImage = movieFromApi.medium_cover_image,
                    mpaRating = movieFromApi.mpa_rating,
                    rating = movieFromApi.rating,
                    runtime = movieFromApi.runtime,
                    smallCoverImage = movieFromApi.small_cover_image,
                    state = movieFromApi.state,
                    summary = movieFromApi.summary,
                    synopsis = movieFromApi.synopsis,
                    title = movieFromApi.title,
                    titleEnglish = movieFromApi.title_english,
                    titleLong = movieFromApi.title_long,
                    year = movieFromApi.year,
                    ytTrailerCode = movieFromApi.yt_trailer_code
                )
            )


            movieFromApi.torrents.forEach { torrentFromApi ->
                torrents.add(
                    Torrent(
                        movieId = movieFromApi.id,
                        hash = torrentFromApi.hash,
                        date_uploaded = torrentFromApi.date_uploaded,
                        date_uploaded_unix = torrentFromApi.date_uploaded_unix,
                        peers = torrentFromApi.peers,
                        quality = torrentFromApi.quality,
                        seeds = torrentFromApi.seeds,
                        size = torrentFromApi.size,
                        size_bytes = torrentFromApi.size_bytes,
                        type = torrentFromApi.type,
                        url = torrentFromApi.url
                    )
                )
            }
        }
        return Pair(movies, torrents)
    }
}