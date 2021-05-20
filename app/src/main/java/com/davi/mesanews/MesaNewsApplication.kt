package com.davi.mesanews

import android.app.Application
import com.davi.mesanews.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.dsl.koinApplication

class MesaNewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MesaNewsApplication)
            modules(
                listOf(
                    newsViewModelModule,
                    networkModule,
                    prefsModule,
                    loginViewModelModule,
                    newsAdapterModule
                )
            )
        }
    }
}