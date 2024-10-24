package com.oleksandrpriadko.backend.data.datasource

import com.oleksandrpriadko.VisibleForTesting
import com.oleksandrpriadko.backend.data.datasource.model.ResponseBackend
import com.oleksandrpriadko.kermit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

interface BackendNetworkDataSource {
    suspend fun observeRaces(isContinuous: Boolean = true): Flow<Result<ResponseBackend>>

    @VisibleForTesting
    fun allowRequest(instant: Instant): Boolean
}

class BackendNetworkDataSourceImpl(
    private val backendApiV1: BackendApiV1,
    // refresh ui interval
    private val uiRefreshInterval: Duration = 1.seconds,
    // request to the backend interval
    private val requestRefreshInterval: Duration = 1.minutes
) : BackendNetworkDataSource {
    override suspend fun observeRaces(isContinuous: Boolean): Flow<Result<ResponseBackend>> {
        // store the time of last request, initialised with now minus requestRefreshInterval so we
        // can perform initial request immediately
        var lastRequestInstant: Instant = Clock.System.now().minus(requestRefreshInterval)
        // store last response, so we can deliver it every uiRefreshInterval to update
        // start time on UI
        var lastResponse: ResponseBackend? = null
        return flow {
            do {
                // make a request to backend only if it was made >= requestRefreshInterval ago
                if (allowRequest(lastRequestInstant)) {
                    val response = backendApiV1.getDataResponseBackend()
                    lastRequestInstant = Clock.System.now()
                    // store last response, so we can deliver it every uiRefreshInterval to update
                    // start time on UI
                    lastResponse = response
                }
                emitResult(this, lastResponse)
                // update UI with uiRefreshInterval delay
                delay(uiRefreshInterval)
            } while (isContinuous)
        }.catch { cause ->
            kermit.e(messageString = "fail getRaces", throwable = cause)
            emit(Result.failure(cause))
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Make request to backend only if it was made >= requestRefreshInterval ago
     */
    override fun allowRequest(instant: Instant): Boolean =
        Clock.System.now().minus(instant) >= requestRefreshInterval

    /**
     * Emit result based on the response from the backend
     */
    private suspend fun emitResult(
        block: FlowCollector<Result<ResponseBackend>>,
        lastResponse: ResponseBackend?
    ) {
        if (lastResponse != null) {
            kermit.d { "success getRaces" }
            block.emit(Result.success(lastResponse))
        } else {
            kermit.d { "fail getRaces" }
            block.emit(Result.failure(NullPointerException("response is null")))
        }
    }
}