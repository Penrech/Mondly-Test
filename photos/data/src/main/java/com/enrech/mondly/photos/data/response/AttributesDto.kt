package com.enrech.mondly.photos.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttributesDto(
    @SerialName("name") val name: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("imageInfo") val imageInfo: ImageInfoDto? = null
)
