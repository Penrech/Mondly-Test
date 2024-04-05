package com.enrech.mondly.photos.domain.entity

import androidx.room.Entity

@Entity
data class PhotoEntity(
    val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String
)
