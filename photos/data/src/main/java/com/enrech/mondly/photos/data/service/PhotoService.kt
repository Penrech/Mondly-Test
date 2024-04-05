package com.enrech.mondly.photos.data.service

import com.enrech.mondly.core.data.entity.MondlyHttpClient
import com.enrech.mondly.photos.data.Urls
import com.enrech.mondly.photos.data.response.ImagesResponseDto
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoService @Inject constructor(
    private val client: MondlyHttpClient
) {
    internal suspend fun getContent(): ImagesResponseDto = client.get().get(Urls.baseUrl).body()
}