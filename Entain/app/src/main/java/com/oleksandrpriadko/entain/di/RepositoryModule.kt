package com.oleksandrpriadko.entain.di

import com.oleksandrpriadko.backend.data.repository.BackendRepositoryImpl
import com.oleksandrpriadko.backend.domain.repository.BackendRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<BackendRepository> { BackendRepositoryImpl(get()) }
}
