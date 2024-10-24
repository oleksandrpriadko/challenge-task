package com.oleksandrpriadko.backend.data.datasource.mapper

import com.oleksandrpriadko.VisibleForTesting
import com.oleksandrpriadko.backend.data.datasource.model.DataBackend
import com.oleksandrpriadko.backend.data.datasource.model.RaceCategory
import com.oleksandrpriadko.backend.data.datasource.model.RaceSummaryBackend
import com.oleksandrpriadko.backend.data.datasource.model.ResponseBackend
import com.oleksandrpriadko.backend.domain.model.Race
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil

fun mapResponseBackend(responseBackend: ResponseBackend?): List<Race> {
    val dataBackendList = responseBackend?.dataBackend
    return if (responseBackend == null || dataBackendList == null) emptyList()
    else mapDataBackend(dataBackendList)
}

fun mapDataBackend(dataBackend: DataBackend?): List<Race> {
    val raceSummaries = dataBackend?.raceSummaries
    return if (raceSummaries.isNullOrEmpty()) emptyList()
    else mapRaceSummariesBackend(raceSummaries)
}

fun mapRaceSummariesBackend(raceSummaries: Map<String?, RaceSummaryBackend?>?): List<Race> =
    raceSummaries?.mapNotNull { mapRaceSummaryBackend(it.value) }.orEmpty()

fun mapRaceSummaryBackend(raceSummaryBackend: RaceSummaryBackend?): Race? {
    val id = raceSummaryBackend?.id
    val seconds = raceSummaryBackend?.advertisedStartBackend?.seconds
    // define category by id
    val category = RaceCategory.entries.find { it.categoryId == raceSummaryBackend?.categoryId }
    return if (id.isNullOrBlank() || seconds == null || category == null) null
    else {
        Race(
            id = id,
            meetingName = raceSummaryBackend.meetingName.orEmpty(),
            startSeconds = seconds,
            timeToStart = defineTimeToStart(
                startSeconds = seconds,
                instantNow = Clock.System.now()
            ),
            number = raceSummaryBackend.number ?: 0,
            category = category
        )
    }
}

/**
 * Format string representation of period between now and event start.
 * Show hours and minutes only if they are positive, show minutes always
 */
@VisibleForTesting
fun defineTimeToStart(startSeconds: Long, instantNow: Instant): String {
    val instantInFuture = Instant.fromEpochSeconds(startSeconds)
    val period = instantNow.periodUntil(instantInFuture, TimeZone.currentSystemDefault())
    // h m s
    // or
    // m s
    // or
    // s
    return buildString {
        if (period.hours > 0) append("${period.hours}h ")
        if (period.minutes > 0) append("${period.minutes}m ")
        append("${period.seconds}s")
    }
}