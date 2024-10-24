package com.oleksandrpriadko.backend.data.repository

import com.oleksandrpriadko.backend.data.datasource.BackendNetworkDataSource
import com.oleksandrpriadko.backend.data.datasource.mapper.mapResponseBackend
import com.oleksandrpriadko.backend.domain.model.Race
import com.oleksandrpriadko.backend.domain.repository.BackendRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BackendRepositoryImpl(
    private val backendNetworkDataSource: BackendNetworkDataSource
) : BackendRepository {
    override suspend fun observerRaces(): Flow<Result<List<Race>>> =
        backendNetworkDataSource.observeRaces()
            .map { result ->
                result.fold(
                    // map data and wrap in result
                    onSuccess = { Result.success(mapResponseBackend(it)) },
                    onFailure = { Result.failure(it) }
                )
            }
}