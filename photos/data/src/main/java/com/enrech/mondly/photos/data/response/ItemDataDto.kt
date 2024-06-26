package com.enrech.mondly.photos.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ItemDataDto(
    @SerialName("id") val id: String? = null,
    @SerialName("attributes") val attributesDto: AttributesDto? = null
)