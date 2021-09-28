package com.stjepanbarisic.refapp.di

import com.stjepanbarisic.refapp.viewmodels.AuthViewModel
import com.stjepanbarisic.refapp.viewmodels.GamesViewModel
import com.stjepanbarisic.refapp.viewmodels.RefereesViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { GamesViewModel(get(), get()) }
    viewModel { RefereesViewModel(get(), get()) }
}
