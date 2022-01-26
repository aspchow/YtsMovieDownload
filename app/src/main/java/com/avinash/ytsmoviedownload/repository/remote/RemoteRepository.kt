package com.avinash.ytsmoviedownload.repository.remote

import java.io.File

class RemoteRepository(private val apiUtility: ApiUtility) {
    suspend fun getMovies(query: String, page: Int) =
            apiUtility.getMovies(query = query, page = page)

    suspend fun getMovies() =
        apiUtility.getMovies(query = "", page = 1)

    suspend fun downloadFile(file: File, url: String) = apiUtility.downloadFile(file, url)
}