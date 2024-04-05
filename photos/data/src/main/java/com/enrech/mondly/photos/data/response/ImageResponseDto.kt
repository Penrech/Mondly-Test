package com.enrech.mondly.photos.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageResponseDto(
    @SerialName("item") val image: ItemDataDto? = null
)
