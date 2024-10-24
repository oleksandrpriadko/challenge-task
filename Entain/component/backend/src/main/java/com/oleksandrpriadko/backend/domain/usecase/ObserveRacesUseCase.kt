package com.oleksandrpriadko.backend.domain.usecase

import com.oleksandrpriadko.VisibleForTesting
import com.oleksandrpriadko.backend.data.datasource.model.RaceCategory
import com.oleksandrpriadko.backend.domain.model.Race
import com.oleksandrpriadko.backend.domain.repository.BackendRepository
import com.oleksandrpriadko.backend.domain.usecase.ObserveRacesUseCase.InputParams
import com.oleksandrpriadko.backend.domain.usecase.ObserveRacesUseCase.Result
import com.oleksandrpriadko.backend.domain.usecase.ObserveRacesUseCase.Result.Error
import com.oleksandrpriadko.backend.domain.usecase.ObserveRacesUseCase.Result.Success
import com.oleksandrpriadko.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

interface ObserveRacesUseCase : UseCase<InputParams, Flow<Result>> {
    @VisibleForTesting
    fun modifyRaces(
        races: List<Race>,
        raceCategories: Set<RaceCategory>,
        requestRefreshInterval: Duration,
        instantNow: Instant = Clock.System.now(),
        maxRaces: Int
    ): List<Race>

    @VisibleForTesting
    fun filterByCategory(raceCategories: Set<RaceCategory>, raceCategory: RaceCategory): Boolean

    @VisibleForTesting
    fun filterByStartTime(
        requestRefreshInterval: Duration,
        instantStart: Instant,
        instantNow: Instant = Clock.System.now()
    ): Boolean

    data class InputParams(val raceCategories: Set<RaceCategory>)

    sealed class Result {
        sealed class Success : Result() {
            data class Filled(val races: List<Race>) : Success()
            data object Empty : Success()
        }

        class Error(val throwable: Throwable) : Result()
    }
}

class ObserveRacesUseCaseImpl(
    private val backendRepository: BackendRepository,
    // request to the backend interval
    private val requestRefreshInterval: Duration = 1.minutes,
    // hardcoded to 5 according to task
    private val maxRaces: Int = 5
) : ObserveRacesUseCase {
    override suspend fun invoke(input: InputParams): Flow<Result> =
        backendRepository.observerRaces()
            .map { racesResult ->
                racesResult.fold(
                    onSuccess = {
                        if (it.isNotEmpty())
                            Success.Filled(
                                modifyRaces(
                                    races = it,
                                    raceCategories = input.raceCategories,
                                    requestRefreshInterval = requestRefreshInterval,
                                    maxRaces = maxRaces
                                )
                            )
                        else
                            Success.Empty
                    },
                    onFailure = { Error(it) }
                )
            }

    /**
     * Filter races by category and start time and sort ascending by start time
     */
    override fun modifyRaces(
        races: List<Race>,
        raceCategories: Set<RaceCategory>,
        requestRefreshInterval: Duration,
        instantNow: Instant,
        maxRaces: Int
    ): List<Race> =
        races
            .filter { race -> filterByCategory(raceCategories, race.category) }
            .filter { race ->
                filterByStartTime(
                    requestRefreshInterval = requestRefreshInterval,
                    instantStart = Instant.fromEpochSeconds(race.startSeconds),
                    instantNow = instantNow
                )
            }
            .sortedBy { it.startSeconds }
            .take(maxRaces)

    override fun filterByCategory(
        raceCategories: Set<RaceCategory>,
        raceCategory: RaceCategory
    ): Boolean = raceCategories.contains(raceCategory)

    /**
     * Check whether the race is not older than requestRefreshInterval by measuring diff between now
     * and event start
     */
    override fun filterByStartTime(
        requestRefreshInterval: Duration,
        instantStart: Instant,
        instantNow: Instant
    ): Boolean {
        return (instantNow - instantStart) <= requestRefreshInterval
    }
}