package com.oleksandrpriadko.backend.domain.usecase

import com.oleksandrpriadko.Helper
import com.oleksandrpriadko.backend.data.datasource.model.RaceCategory
import com.oleksandrpriadko.backend.domain.model.Race
import com.oleksandrpriadko.backend.domain.repository.BackendRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.mockk
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.minutes

class ObserveRacesUseCaseImplTest : BehaviorSpec({
    val startSeconds = 1729689240L
    val interval = 1.minutes
    // filterByStartTime
    Given("filter by start time, in range") {
        val repository = mockk<BackendRepository>()
        val useCase = ObserveRacesUseCaseImpl(repository)
        When("filterByStartTime") {
            Then("in range") {
                useCase.filterByStartTime(
                    requestRefreshInterval = interval,
                    instantStart = Instant.fromEpochSeconds(startSeconds),
                    instantNow = (Instant.fromEpochSeconds(startSeconds).plus(interval / 2))
                ).shouldBeTrue()
            }
        }
    }
    Given("filter by start time, out of range") {
        val repository = mockk<BackendRepository>()
        val useCase = ObserveRacesUseCaseImpl(repository)
        When("filterByStartTime") {
            Then("out of range") {
                useCase.filterByStartTime(
                    requestRefreshInterval = interval,
                    instantStart = Instant.fromEpochSeconds(startSeconds),
                    instantNow = (Instant.fromEpochSeconds(startSeconds).plus(interval * 2))
                ).shouldBeFalse()
            }
        }
    }

    // filterByCategory
    Given("filterByCategory, no suitable category") {
        val repository = mockk<BackendRepository>()
        val useCase = ObserveRacesUseCaseImpl(repository)
        When("filterByCategory") {
            Then("no suitable category") {
                useCase.filterByCategory(
                    raceCategory = RaceCategory.HORSE,
                    raceCategories = setOf(RaceCategory.HARNESS)
                ).shouldBeFalse()
            }
        }
    }
    Given("filterByCategory, has suitable category") {
        val repository = mockk<BackendRepository>()
        val useCase = ObserveRacesUseCaseImpl(repository)
        When("filterByCategory") {
            Then("has suitable category") {
                useCase.filterByCategory(
                    raceCategory = RaceCategory.HORSE,
                    raceCategories = setOf(RaceCategory.HARNESS, RaceCategory.HORSE)
                ).shouldBeTrue()
            }
        }
    }

    // modifyRaces
    Given("modifyRaces, no suitable races") {
        val repository = mockk<BackendRepository>()
        val useCase = ObserveRacesUseCaseImpl(repository)
        When("modifyRaces") {
            Then("no suitable races") {
                val result = useCase.modifyRaces(
                    races = Helper.getRaces(),
                    raceCategories = setOf(RaceCategory.HARNESS),
                    requestRefreshInterval = 1.minutes,
                    maxRaces = 3
                )
                result.shouldBeEmpty()
            }
        }
    }
    Given("modifyRaces, has suitable races") {
        val repository = mockk<BackendRepository>()
        val useCase = ObserveRacesUseCaseImpl(repository)
        When("modifyRaces") {
            Then("has suitable races") {
                val result = useCase.modifyRaces(
                    races = listOf(
                        Race(
                            id = "",
                            meetingName = "first name",
                            number = 1,
                            startSeconds = startSeconds,
                            timeToStart = "50s",
                            category = RaceCategory.HARNESS
                        ),
                        Race(
                            id = "",
                            meetingName = "first name",
                            number = 1,
                            startSeconds = startSeconds,
                            timeToStart = "50s",
                            category = RaceCategory.HARNESS
                        ),
                        Race(
                            id = "",
                            meetingName = "second name",
                            number = 2,
                            startSeconds = startSeconds,
                            timeToStart = "40s",
                            category = RaceCategory.GREYHOUND
                        )
                    ),
                    raceCategories = setOf(RaceCategory.HARNESS, RaceCategory.GREYHOUND),
                    requestRefreshInterval = 1.minutes,
                    instantNow = (Instant.fromEpochSeconds(startSeconds).minus(2.minutes)),
                    maxRaces = 3
                )
                result.shouldHaveSize(3)
            }
        }
    }
})
