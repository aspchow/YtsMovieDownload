package com.avinash.ytsmoviedownload.repository.local.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = Movie::class, parentColumns = arrayOf("id"), childColumns = arrayOf("movieId"), onDelete = ForeignKey.CASCADE)])
data class Torrent(
    val movieId : Int,
    @PrimaryKey
    val hash: String,
    val date_uploaded: String,
    val date_uploaded_unix: Int,
    val peers: Int,
    val quality: String,
    val seeds: Int,
    val size: String,
    val size_bytes: Long,
    val type: String,
    val url: String
)
