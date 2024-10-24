package com.oleksandrpriadko.entain.di

import com.oleksandrpriadko.entain.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MainViewModel)
}