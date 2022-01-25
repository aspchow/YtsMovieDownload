package com.avinash.ytsmoviedownload.repository.remote

class RemoteRepository(private val apiUtility: ApiUtility) {
    suspend fun getMovies() = apiUtility.getMovies()
}