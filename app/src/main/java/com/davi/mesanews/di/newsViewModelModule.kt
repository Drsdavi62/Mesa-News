package com.davi.mesanews.di

import com.davi.mesanews.news.NewsViewModel
import com.davi.mesanews.repository.FavoritesRepository
import com.davi.mesanews.utils.room.dao.DatabaseDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val newsViewModelModule = module {

    single<FavoritesRepository>  {
        DatabaseDataSource(androidContext())
    } bind FavoritesRepository::class

    viewModel {
        NewsViewModel(get(), get())
    }
}