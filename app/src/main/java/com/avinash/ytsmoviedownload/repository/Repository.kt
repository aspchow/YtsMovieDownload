package com.avinash.ytsmoviedownload.repository

import com.avinash.ytsmoviedownload.repository.local.LocalRepository
import com.avinash.ytsmoviedownload.repository.local.database.model.Torrent
import com.avinash.ytsmoviedownload.repository.remote.RemoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class Repository(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val moviesMapper : MovieMapper
) {





    fun getMoviesLike(search : String) = localRepository.getMovies(search)


    suspend fun getMoviesFromServer() = flow {
        val response = remoteRepository.getMovies().first()
        response.onSuccess { apiResponse ->
            val (movies , torrents) = moviesMapper.mapMoviesFromResponse(apiResponse.data.movies)

            localRepository.apply {
                upsertMovies(movies)
                upsertTorrents(torrents)
            }
            emit(ApiState.Success)
        }

        response.onFailure { exception->
            emit(ApiState.Failure(response.exceptionOrNull()!!))
        }
    }

    fun getTorrents(movieId: Int): Flow<List<Torrent>> = localRepository.getTorrents(movieId = movieId)
}


sealed class ApiState{
    object Success : ApiState()
    class Failure(val exception: Throwable) : ApiState()
}