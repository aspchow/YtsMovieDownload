package com.avinash.ytsmoviedownload.repository.local.database.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val data: Data,
    val status: String,
    val status_message: String
)