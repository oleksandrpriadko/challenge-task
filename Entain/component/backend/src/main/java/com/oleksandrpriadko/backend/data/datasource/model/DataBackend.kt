package com.oleksandrpriadko.backend.data.datasource.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataBackend(
    @SerialName("race_summaries")
    var raceSummaries: Map<String?, RaceSummaryBackend?>? = null
)