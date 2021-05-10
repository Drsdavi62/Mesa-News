package com.davi.mesanews.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val prefsModule = module {
    single {
        providePrefs(androidContext())
    }
}

fun providePrefs(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)