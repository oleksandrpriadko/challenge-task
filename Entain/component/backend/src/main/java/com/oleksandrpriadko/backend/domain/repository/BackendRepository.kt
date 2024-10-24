package com.oleksandrpriadko.backend.domain.repository

import com.oleksandrpriadko.backend.domain.model.Race
import kotlinx.coroutines.flow.Flow

interface BackendRepository {
    suspend fun observerRaces(): Flow<Result<List<Race>>>
}