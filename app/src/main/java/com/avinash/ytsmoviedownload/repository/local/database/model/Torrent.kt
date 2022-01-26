package com.avinash.ytsmoviedownload.repository.local.database.model

import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.avinash.ytsmoviedownload.utils.globalContext
import org.koin.core.annotation.KoinInternalApi
import java.io.File

@Entity(
    foreignKeys = [ForeignKey(
        entity = Movie::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("movieId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Torrent(
    val movieId: Int,
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

@KoinInternalApi
val Torrent.uriFile: Uri
    get() = file.toUri()
@KoinInternalApi
val Torrent.file: File
    get() = File(globalContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "$movieId+$hash")
