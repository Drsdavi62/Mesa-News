package com.davi.mesanews

import android.app.Application
import com.davi.mesanews.di.loginViewModelModule
import com.davi.mesanews.di.networkModule
import com.davi.mesanews.di.newsViewModelModule
import com.davi.mesanews.di.prefsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MesaNewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(
                listOf(
                    newsViewModelModule,
                    networkModule,
                    prefsModule,
                    loginViewModelModule
                )
            )
        }
    }
}