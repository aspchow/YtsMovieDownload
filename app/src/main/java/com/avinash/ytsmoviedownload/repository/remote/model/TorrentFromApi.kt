package com.avinash.ytsmoviedownload.repository.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class TorrentFromApi(
    val date_uploaded: String,
    val date_uploaded_unix: Int,
    val hash: String,
    val peers: Int,
    val quality: String,
    val seeds: Int,
    val size: String,
    val size_bytes: Long,
    val type: String,
    val url: String
)