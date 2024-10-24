package com.oleksandrpriadko.entain.di

import com.oleksandrpriadko.backend.domain.usecase.ObserveRacesUseCase
import com.oleksandrpriadko.backend.domain.usecase.ObserveRacesUseCaseImpl
import org.koin.dsl.module
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

val useCaseModule = module {
    single<ObserveRacesUseCase> {
        ObserveRacesUseCaseImpl(
            backendRepository = get(),
            requestRefreshInterval = requestRefreshInterval,
            maxRaces = maxRaces
        )
    }
}

val requestRefreshInterval: Duration = 1.minutes
val uiRefreshInterval: Duration = 1.seconds
const val maxRaces: Int = 5
