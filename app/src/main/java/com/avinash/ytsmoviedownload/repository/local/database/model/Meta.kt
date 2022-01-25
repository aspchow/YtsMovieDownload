package com.avinash.ytsmoviedownload.repository.local.database.model

import kotlinx.serialization.Serializable

@Serializable
data class Meta(
    val api_version: Int,
    val execution_time: String,
    val server_time: Int,
    val server_timezone: String
)