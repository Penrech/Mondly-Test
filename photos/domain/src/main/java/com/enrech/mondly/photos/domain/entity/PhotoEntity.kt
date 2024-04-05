package com.enrech.mondly.photos.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PhotoEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String
)
