package com.avinash.ytsmoviedownload.repository.local.database

import androidx.room.*
import com.avinash.ytsmoviedownload.repository.local.database.model.Movie
import com.avinash.ytsmoviedownload.repository.local.database.model.Torrent
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMovies(movies: List<Movie>)

    @Delete
    suspend fun deleteMovies(movies: List<Movie>)

    @Query("SELECT * FROM Movie")
    fun getMovies() : Flow<List<Movie>>


    @Query("SELECT * FROM Movie WHERE title LIKE '%' || :searchQuery || '%' ORDER BY dateUploadedUnix DESC")
    fun getMoviesLike(searchQuery : String) : Flow<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun upsertTorrents(torrents: List<Torrent>)

    @Delete
   suspend fun deleteTorrents(torrents: List<Torrent>)

    @Query("SELECT * FROM Torrent WHERE movieId = :movieId ORDER BY size_bytes DESC")
    fun getMovieTorrents(movieId : Int) : Flow<List<Torrent>>
}