package com.enrech.mondly.photos.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageInfoDto(
    @SerialName("imageUrl") val imageUrl: String? = null
)
