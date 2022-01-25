package com.avinash.ytsmoviedownload.repository.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.avinash.ytsmoviedownload.repository.local.database.model.Movie
import com.avinash.ytsmoviedownload.repository.local.database.model.StringToListConverter
import com.avinash.ytsmoviedownload.repository.local.database.model.Torrent
import com.avinash.ytsmoviedownload.repository.remote.model.MovieFromApi

@Database(entities = [Movie::class, Torrent::class], version = 2, exportSchema = true)
@TypeConverters(StringToListConverter::class)
abstract class YtsMovieDatabase : RoomDatabase() {
    abstract fun getMoviesDao() : MoviesDao
}