package com.oleksandrpriadko

import com.oleksandrpriadko.backend.data.datasource.model.AdvertisedStartBackend
import com.oleksandrpriadko.backend.data.datasource.model.DataBackend
import com.oleksandrpriadko.backend.data.datasource.model.RaceCategory
import com.oleksandrpriadko.backend.data.datasource.model.RaceSummaryBackend
import com.oleksandrpriadko.backend.data.datasource.model.ResponseBackend
import com.oleksandrpriadko.backend.domain.model.Race

object Helper {
    fun getResponseBackend(): ResponseBackend =
        ResponseBackend(
            dataBackend = DataBackend(
                raceSummaries = mapOf(
                    "0" to RaceSummaryBackend(
                        id = "0",
                        number = 1,
                        meetingName = "name",
                        advertisedStartBackend = AdvertisedStartBackend(seconds = 100L),
                        categoryId = RaceCategory.HORSE.categoryId
                    ),
                    "1" to RaceSummaryBackend(
                        id = "1",
                        number = 2,
                        meetingName = "name1",
                        advertisedStartBackend = AdvertisedStartBackend(seconds = 100L),
                        categoryId = RaceCategory.HORSE.categoryId
                    )
                )
            )
        )

    fun getRaces(): List<Race> =
        listOf(
            Race(
                id = "",
                meetingName = "first name",
                number = 1,
                startSeconds = 0,
                timeToStart = "50s",
                category = RaceCategory.HORSE
            ),
            Race(
                id = "",
                meetingName = "second name",
                number = 2,
                startSeconds = 0,
                timeToStart = "40s",
                category = RaceCategory.GREYHOUND
            ),
            Race(
                id = "",
                meetingName = "third name",
                number = 3,
                startSeconds = 0,
                timeToStart = "30s",
                category = RaceCategory.GREYHOUND
            ),
            Race(
                id = "",
                meetingName = "fourth name",
                number = 4,
                startSeconds = 0,
                timeToStart = "30s",
                category = RaceCategory.HORSE
            ),
            Race(
                id = "",
                meetingName = "fifth name",
                number = 5,
                startSeconds = 0,
                timeToStart = "30s",
                category = RaceCategory.GREYHOUND
            )
        )
}