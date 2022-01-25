package com.avinash.ytsmoviedownload.application

import android.app.Application
import com.avinash.ytsmoviedownload.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class YtsMovieDownloaderApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@YtsMovieDownloaderApplication)
            modules(appModule)
        }
    }
}