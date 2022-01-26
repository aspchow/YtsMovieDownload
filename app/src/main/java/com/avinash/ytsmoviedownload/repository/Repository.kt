package com.avinash.ytsmoviedownload.repository

import com.avinash.ytsmoviedownload.repository.local.LocalRepository
import com.avinash.ytsmoviedownload.repository.local.database.model.Torrent
import com.avinash.ytsmoviedownload.repository.remote.RemoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class Repository(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val moviesMapper: MovieMapper
) {


    lateinit var downloadMoviesJob: Job

    fun getTorrents(movieId: Int): Flow<List<Torrent>> =
        localRepository.getTorrents(movieId = movieId)

    fun getMoviesLike(search: String) = localRepository.getMovies(search)

    suspend fun getMoviesFromServer() = flow<ApiState> {
        val response = remoteRepository.getMovies().first()
        response.onSuccess { apiResponse ->
            val (movies, torrents) = moviesMapper.mapMoviesFromResponse(apiResponse.data.movies)

            localRepository.apply {
                upsertMovies(movies)
                upsertTorrents(torrents)
            }
            emit(ApiState.Success)
        }

        response.onFailure {
            emit(ApiState.Failure(response.exceptionOrNull()!!))
        }
    }


    suspend fun getMoviesFromServer(query: String) = flow {
        try {
            getTheMoviesAndSave(query = query, 1)
            emit(ApiState.Success)
        } catch (exception: Exception) {
            emit(ApiState.Failure(exception = exception))
        }
    }


    private suspend fun getTheMoviesAndSave(query: String, page: Int) {
        val response = remoteRepository.getMovies(query = query, page = page).first()
        response.onSuccess { apiResponse ->
            val (movies, torrents) = moviesMapper.mapMoviesFromResponse(apiResponse.data.movies)

            localRepository.apply {
                upsertMovies(movies)
                upsertTorrents(torrents)
            }

            val data = apiResponse.data
            if (data.movie_count < data.movie_count * data.page_number) {
                getTheMoviesAndSave(query = query, page = page + 1)
            }
        }
    }

    suspend fun downloadFile(file: File, url: String) = remoteRepository.downloadFile(file, url)

}


sealed class ApiState {
    object Success : ApiState()
    object InProgress : ApiState()
    object Cancelled : ApiState()
    class Failure(val exception: Throwable) : ApiState()
}