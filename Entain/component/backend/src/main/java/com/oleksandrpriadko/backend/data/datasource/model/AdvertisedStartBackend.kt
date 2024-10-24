package com.oleksandrpriadko.backend.data.datasource.model

import kotlinx.serialization.Serializable

@Serializable
data class AdvertisedStartBackend(
    var seconds: Long? = null
)