package com.enrech.mondly.photos.data.mapper

import com.enrech.mondly.core.domain.mapper.BaseMapper
import com.enrech.mondly.photos.data.response.ImagesResponseDto
import com.enrech.mondly.photos.domain.entity.PhotoEntity
import javax.inject.Inject

class PhotoResponseMapper @Inject constructor(): BaseMapper<ImagesResponseDto, List<PhotoEntity>>() {
    override fun mapFrom(from: ImagesResponseDto): List<PhotoEntity> =
        from.items?.let {
            it.mapNotNull { item ->
                item.id?.toLongOrNull()?.let {  id ->
                    PhotoEntity(
                        id = id,
                        name = item.attributesDto?.name.orEmpty(),
                        description = item.attributesDto?.description.orEmpty(),
                        imageUrl = item.attributesDto?.imageInfo?.imageUrl.orEmpty()
                    )
                }
            }
        } ?: emptyList()
}