package com.enrech.mondly.photos.domain.usecase

import com.enrech.mondly.photos.domain.repository.PhotosRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetPhotosFlowUseCase @Inject constructor(
    private val photosRepository: PhotosRepository
) {
    operator fun invoke() = photosRepository.getPhotosFlow()
}