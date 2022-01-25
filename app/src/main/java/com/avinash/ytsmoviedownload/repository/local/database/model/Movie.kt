package com.avinash.ytsmoviedownload.repository.local.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Movie(
    @PrimaryKey
    val id: Int,
    val backgroundImage: String,
    val backgroundImageOriginal: String,
    val dateUploaded: String,
    val dateUploadedUnix: Int,
    val descriptionFull: String,
    val genres: List<String>,
    val imdbCode: String,
    val language: String,
    val largeCoverImage: String,
    val mediumCoverImage: String,
    val mpaRating: String,
    val rating: Double,
    val runtime: Int,
    val smallCoverImage: String,
    val state: String,
    val summary: String,
    val synopsis: String,
    val title: String,
    val titleEnglish: String,
    val titleLong: String,
    val year: Int,
    val ytTrailerCode: String
)


class StringToListConverter{

    @TypeConverter
    fun toListOfGenres(genres: String): List<String> {
        return listOf()
    }

    @TypeConverter
    fun toStringOfGenres(genres: List<String>): String {
        return ""
    }



}