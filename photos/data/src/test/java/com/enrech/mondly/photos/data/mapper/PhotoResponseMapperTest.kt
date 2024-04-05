package com.enrech.mondly.photos.data.mapper

import com.enrech.mondly.photos.data.response.AttributesDto
import com.enrech.mondly.photos.data.response.ImageResponseDto
import com.enrech.mondly.photos.data.response.ImagesResponseDto
import com.enrech.mondly.photos.data.response.ItemDataDto
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PhotoResponseMapperTest {
    private lateinit var mapper: PhotoResponseMapper

    @Before
    fun setUp() {
        mapper = PhotoResponseMapper()
    }

    @Test
    fun `given response have null items, expect empty list`() = runTest {
        val response = mockk<ImagesResponseDto> {
            every { items } returns null
        }

        val result = mapper.mapFrom(response)

        Truth.assertThat(result.isEmpty())
    }

    @Test
    fun `given response contains some items without id, expect list with just the ones with id`() = runTest {
        val itemWithoutIdName = "item without Id"
        val itemWithoutId = ImageResponseDto(
            ItemDataDto(
                attributesDto = AttributesDto(
                    name = itemWithoutIdName
                )
            )
        )

        val itemWithIdName = "item with id"
        val itemWithId = ImageResponseDto(
            ItemDataDto(
                id = "1",
                attributesDto = AttributesDto(
                    name = itemWithIdName
                )
            )
        )

        val response = mockk<ImagesResponseDto> {
            every { items } returns listOf(itemWithoutId, itemWithId)
        }

        val result = mapper.mapFrom(response)

        assert(result.any { it.name == itemWithIdName})
        assert(result.none { it.name == itemWithoutIdName })
    }
}