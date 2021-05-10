package com.davi.mesanews.di

import androidx.navigation.NavController
import com.davi.mesanews.home.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginViewModelModule = module {
    viewModel { (navController: NavController) ->
        LoginViewModel(
            apiHandler = get(),
            prefs = get(),
            navController
        )
    }
}