package com.avinash.ytsmoviedownload.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.avinash.ytsmoviedownload.repository.ApiState
import com.avinash.ytsmoviedownload.repository.Repository
import com.avinash.ytsmoviedownload.repository.local.database.model.Movie
import com.avinash.ytsmoviedownload.repository.local.database.model.Torrent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File

class YtsViewModel(val repository: Repository) : ViewModel() {

    lateinit var downloadMoviesJob: Job

    val searchContent: MutableStateFlow<String> = MutableStateFlow("")

    val moviesSearchProgress: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Success)

    fun setSearch(searchContent: String) {
        this.searchContent.value = searchContent
    }

    fun getTorrents(movieId: Int): Flow<List<Torrent>> = repository.getTorrents(movieId = movieId)

    suspend fun downloadFile(file: File, url: String) = repository.downloadFile(file, url)

    val movies: Flow<List<Movie>> = searchContent
        .debounce(1000)
        .distinctUntilChanged()
        .flatMapLatest { searchContent ->
            if (this::downloadMoviesJob.isInitialized) {
                downloadMoviesJob.cancel()
            }
           if(searchContent.isNotEmpty()){
               downloadMoviesJob = viewModelScope.launch(Dispatchers.IO) {
                   repository.getMoviesFromServer(query = searchContent).collect { apiState ->
                       moviesSearchProgress.value = apiState
                   }
               }
           }
            repository.getMoviesLike(searchContent)
        }

}