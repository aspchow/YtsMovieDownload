package com.avinash.ytsmoviedownload.repository.local

import com.avinash.ytsmoviedownload.repository.local.database.MoviesDao
import com.avinash.ytsmoviedownload.repository.local.database.model.Movie
import com.avinash.ytsmoviedownload.repository.local.database.model.Torrent
import kotlinx.coroutines.flow.Flow

class LocalRepository(private val moviesDao: MoviesDao) {

    fun getMovies(searchQuery: String) = moviesDao.getMoviesLike(searchQuery = searchQuery)


    suspend fun upsertMovies(movies: List<Movie>) = moviesDao.upsertMovies(movies)

    suspend fun upsertTorrents(torrents: List<Torrent>) = moviesDao.upsertTorrents(torrents)


    fun getTorrents(movieId: Int): Flow<List<Torrent>> = moviesDao.getMovieTorrents(movieId = movieId)
}