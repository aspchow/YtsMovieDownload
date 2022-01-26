package com.avinash.ytsmoviedownload.repository.remote

import java.io.File

class RemoteRepository(private val apiUtility: ApiUtility) {
    suspend fun getMovies() = apiUtility.getMovies()
    suspend fun downloadFile(file: File, url: String) = apiUtility.downloadFile(file, url)
}