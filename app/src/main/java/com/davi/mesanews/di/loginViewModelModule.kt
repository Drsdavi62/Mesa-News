package com.davi.mesanews.di

import com.davi.mesanews.home.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginViewModelModule = module {
    viewModel {
        LoginViewModel(
            retrofitClient = get(),
            prefs = get(),
        )
    }
}