package com.davi.mesanews.di

import com.davi.mesanews.news.NewsViewModel
import com.davi.mesanews.utils.retrofit.APIHandler
import com.davi.mesanews.utils.room.dao.FavoritesRepository
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.experimental.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val newsViewModelModule = module {

    single {
        FavoritesRepository(androidContext())
    }

    viewModel {
        NewsViewModel(get(), get())
    }
}