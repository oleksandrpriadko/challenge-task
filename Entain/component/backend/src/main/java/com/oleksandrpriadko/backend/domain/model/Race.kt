package com.oleksandrpriadko.backend.domain.model

import com.oleksandrpriadko.backend.data.datasource.model.RaceCategory

data class Race(
    val id: String,
    val meetingName: String,
    val number: Int,
    val startSeconds: Long,
    // string representation of the start time
    val timeToStart: String,
    val category: RaceCategory
)