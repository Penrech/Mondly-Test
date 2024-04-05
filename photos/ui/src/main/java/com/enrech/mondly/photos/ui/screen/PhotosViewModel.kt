package com.enrech.mondly.photos.ui.screen

import com.enrech.mondly.photos.domain.entity.PhotoEntity
import com.enrech.mondly.shared_types.Action
import com.enrech.mondly.shared_types.Effect
import com.enrech.mondly.shared_types.ScreenState
import com.enrech.mondly.viewmodel_util.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(): BaseViewModel<PhotosState, PhotoAction, PhotoEffect>() {
    override fun createInitialScreenState(): PhotosState = PhotosState()

    override suspend fun handleActions(action: PhotoAction) {
        TODO("Not yet implemented")
    }

}
data class PhotosState(
    val isInitializing: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: Throwable? = null,
    val data: List<PhotoEntity> = emptyList()
): ScreenState
sealed class PhotoAction: Action
sealed class PhotoEffect: Effect