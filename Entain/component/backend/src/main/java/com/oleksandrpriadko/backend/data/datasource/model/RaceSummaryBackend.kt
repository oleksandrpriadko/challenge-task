package com.oleksandrpriadko.backend.data.datasource.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RaceSummaryBackend(
    @SerialName("race_id")
    var id: String? = null,
    @SerialName("meeting_name")
    var meetingName: String? = null,
    @SerialName("race_number")
    var number: Int? = null,
    @SerialName("advertised_start")
    var advertisedStartBackend: AdvertisedStartBackend? = null,
    @SerialName("category_id")
    var categoryId: String? = null
)