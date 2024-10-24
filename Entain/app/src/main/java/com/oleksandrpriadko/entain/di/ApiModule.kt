package com.oleksandrpriadko.entain.di

import com.oleksandrpriadko.backend.data.datasource.BackendApiV1
import com.oleksandrpriadko.backend.data.datasource.BackendApiV1Impl
import com.oleksandrpriadkoentain.R
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val apiModule = module {
    single<BackendApiV1> {
        BackendApiV1Impl(hostUrl = androidContext().getString(R.string.base_url))
    }
}
