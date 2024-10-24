package com.oleksandrpriadko.backend.data.datasource.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseBackend(
    @SerialName("data")
    var dataBackend: DataBackend? = null
)

