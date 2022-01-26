package com.avinash.ytsmoviedownload.repository.local.database.model

import com.avinash.ytsmoviedownload.repository.remote.model.MovieFromApi
import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val movie_count: Int,
    val movies: List<MovieFromApi> = emptyList(),
    val limit: Int,
    val page_number: Int
)