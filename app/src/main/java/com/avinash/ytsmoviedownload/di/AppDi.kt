package com.avinash.ytsmoviedownload.di

import androidx.room.Room
import com.avinash.ytsmoviedownload.repository.MovieMapper
import com.avinash.ytsmoviedownload.repository.remote.RemoteRepository
import com.avinash.ytsmoviedownload.repository.Repository
import com.avinash.ytsmoviedownload.repository.local.LocalRepository
import com.avinash.ytsmoviedownload.repository.local.database.MoviesDao
import com.avinash.ytsmoviedownload.repository.local.database.YtsMovieDatabase
import com.avinash.ytsmoviedownload.repository.remote.ApiUtility
import com.avinash.ytsmoviedownload.ui.theme.YtsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    single {
        Repository(get(), get(), get())
    }

    single {
        MovieMapper()
    }

    single {
        Room.databaseBuilder(get(), YtsMovieDatabase::class.java, "yts_app_database").build().getMoviesDao()
    }

    single {
        LocalRepository(get())
    }


    single {
        ApiUtility()
    }

    single {
        RemoteRepository(get())
    }

    viewModel {
        YtsViewModel(get())
    }

}