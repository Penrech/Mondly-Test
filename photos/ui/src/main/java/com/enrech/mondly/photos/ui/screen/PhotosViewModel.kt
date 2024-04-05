package com.enrech.mondly.photos.ui.screen

import androidx.lifecycle.viewModelScope
import com.enrech.mondly.core.domain.exception.NoInternetException
import com.enrech.mondly.photos.domain.entity.PhotoEntity
import com.enrech.mondly.photos.domain.usecase.UpdatePhotosUseCase
import com.enrech.mondly.shared_types.Action
import com.enrech.mondly.shared_types.Effect
import com.enrech.mondly.shared_types.ScreenState
import com.enrech.mondly.viewmodel_util.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val updatePhotosUseCase: UpdatePhotosUseCase
): BaseViewModel<PhotosState, PhotoAction, PhotoEffect>() {
    override fun createInitialScreenState(): PhotosState = PhotosState()

    init {
        refreshFlow
            .onEach {
                setScreenState {
                    currentScreenState.copy(
                        isRefreshing = !isInitializing,
                        isInitializing = currentScreenState.isInitializing || currentScreenState.error != null,
                        error = null
                    )
                }
            }.mapLatest {
                updatePhotosUseCase()
                    .fold(
                        onSuccess = {
                            if (it.secondaryError != null) {
                                setEffect {
                                    PhotoEffect.OnSecondaryError(it.secondaryError is NoInternetException)
                                }
                            }

                            currentScreenState.copy(
                                isRefreshing = false,
                                isInitializing = false,
                                data = it.data
                            )
                        },
                        onFailure = {
                            currentScreenState.copy(
                                isRefreshing = false,
                                isInitializing = false,
                                error = it
                            )
                        }
                    )
            }
            .flowOn(Dispatchers.IO)
            .onEach {
                setScreenState { it }
            }
            .launchIn(viewModelScope)

    }

    override suspend fun handleActions(action: PhotoAction) {
        when(action) {
            PhotoAction.Refresh -> refresh()
        }
    }

}
data class PhotosState(
    val isInitializing: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: Throwable? = null,
    val data: List<PhotoEntity> = emptyList()
): ScreenState
sealed class PhotoAction: Action {
    data object Refresh: PhotoAction()
}
sealed class PhotoEffect: Effect {
    data class OnSecondaryError(val isNetworkError: Boolean): PhotoEffect()
}