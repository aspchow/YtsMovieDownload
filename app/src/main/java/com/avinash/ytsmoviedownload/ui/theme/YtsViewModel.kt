package com.avinash.ytsmoviedownload.ui.theme

import androidx.lifecycle.ViewModel
import com.avinash.ytsmoviedownload.repository.Repository
import com.avinash.ytsmoviedownload.repository.local.database.model.Movie
import com.avinash.ytsmoviedownload.repository.local.database.model.Torrent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class YtsViewModel(val repository: Repository) : ViewModel() {

    val searchContent: MutableStateFlow<String> = MutableStateFlow("")

    fun setSearch(searchContent: String){
        this.searchContent.value = searchContent
    }

    private var _selectedMovie : Movie? = null

    fun getSelectedMovie(): Movie = _selectedMovie!!
    fun setSelectedMovie(movie: Movie){
        _selectedMovie = movie
    }

    fun getTorrents(movieId: Int) : Flow<List<Torrent>> = repository.getTorrents(movieId = movieId)

    val movies: Flow<List<Movie>> = searchContent.flatMapLatest { searchContent ->
        repository.getMoviesLike(searchContent)
    }

}