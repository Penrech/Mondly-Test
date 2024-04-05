package com.enrech.mondly.photos.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImagesResponseDto(
    @SerialName("dataCollection") val items: List<ImageResponseDto>? = null
)
