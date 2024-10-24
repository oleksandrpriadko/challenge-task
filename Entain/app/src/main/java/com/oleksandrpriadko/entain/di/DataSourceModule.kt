package com.oleksandrpriadko.entain.di

import com.oleksandrpriadko.backend.data.datasource.BackendNetworkDataSource
import com.oleksandrpriadko.backend.data.datasource.BackendNetworkDataSourceImpl
import org.koin.dsl.module

val dataSourceModule = module {
    single<BackendNetworkDataSource> {
        BackendNetworkDataSourceImpl(
            backendApiV1 = get(),
            uiRefreshInterval = uiRefreshInterval,
            requestRefreshInterval = requestRefreshInterval
        )
    }
}
