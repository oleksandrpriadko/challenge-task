package com.oleksandrpriadko.backend.data.datasource.mapper

import com.oleksandrpriadko.Helper
import com.oleksandrpriadko.backend.data.datasource.model.RaceCategory
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.minutes

class MapperTest : BehaviorSpec({
    // mapResponseBackend
    Given("responseBackend is null") {
        When("mapResponseBackend") {
            Then("return empty list") {
                mapResponseBackend(null).shouldBeEmpty()
            }
        }
    }
    Given("responseBackend is filled") {
        When("mapResponseBackend") {
            Then("return races") {
                val responseBackend = Helper.getResponseBackend()
                mapResponseBackend(responseBackend).shouldHaveSize(2)
            }
        }
    }

    // mapDataBackend
    Given("dataBackend is null") {
        When("mapDataBackend") {
            Then("return empty list") {
                mapDataBackend(null).shouldBeEmpty()
            }
        }
    }
    Given("dataBackend is filled") {
        When("mapDataBackend") {
            Then("return races") {
                val responseBackend = Helper.getResponseBackend()
                mapDataBackend(responseBackend.dataBackend).shouldHaveSize(2)
            }
        }
    }

    // mapRaceSummariesBackend
    Given("raceSummaries is null") {
        When("mapRaceSummariesBackend") {
            Then("return empty list") {
                mapRaceSummariesBackend(null).shouldBeEmpty()
            }
        }
    }
    Given("raceSummaries is filled") {
        When("mapRaceSummariesBackend") {
            Then("return races") {
                val responseBackend = Helper.getResponseBackend()
                mapRaceSummariesBackend(responseBackend.dataBackend?.raceSummaries).shouldHaveSize(2)
            }
        }
    }

    // mapRaceSummaryBackend
    Given("raceSummaryBackend is null") {
        When("mapRaceSummaryBackend") {
            Then("return empty list") {
                mapRaceSummaryBackend(null).shouldBeNull()
            }
        }
    }
    Given("raceSummaryBackend is filled") {
        When("mapRaceSummaryBackend") {
            Then("return valid race") {
                val responseBackend = Helper.getResponseBackend()
                val race =
                    mapRaceSummaryBackend(responseBackend.dataBackend?.raceSummaries?.get("0"))
                race.shouldNotBeNull()
                race.id.shouldBe("0")
                race.number.shouldBe(1)
                race.meetingName.shouldBe("name")
                race.category.shouldBe(RaceCategory.HORSE)
                race.startSeconds.shouldBe(100L)
            }
        }
    }
    Given("startSeconds to string representation") {
        When("defineTimeToStart") {
            Then("return valid string representation") {
                val startSeconds = 1729689240L
                val timeToStart =
                    defineTimeToStart(
                        startSeconds = startSeconds,
                        instantNow = Instant.fromEpochSeconds(startSeconds).minus(1.minutes)
                    )
                timeToStart.shouldBe("1m 0s")
            }
        }
    }
})
