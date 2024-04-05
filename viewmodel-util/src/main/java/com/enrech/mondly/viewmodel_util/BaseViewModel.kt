package com.enrech.mondly.viewmodel_util

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enrech.mondly.shared_types.Action
import com.enrech.mondly.shared_types.ClickAction
import com.enrech.mondly.shared_types.Effect
import com.enrech.mondly.shared_types.ScreenState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : ScreenState, A : Action, E : Effect> : ViewModel() {

    private val initialScreenState: S by lazy { createInitialScreenState() }
    protected abstract fun createInitialScreenState(): S

    private val lastClickEvent: MutableStateFlow<Long> = MutableStateFlow(System.currentTimeMillis())

    protected val currentScreenState: S get() = screenState.value

    private val _screenState: MutableStateFlow<S> by lazy { MutableStateFlow(initialScreenState) }
    val screenState by lazy { _screenState.asStateFlow() }

    private val _actions: MutableSharedFlow<A> = MutableSharedFlow()
    protected val actions = _actions.asSharedFlow()

    private val _effect: Channel<E> = Channel()
    val effect = _effect.receiveAsFlow()

    private val _refreshFlow = MutableSharedFlow<RefreshState>()
    protected val refreshFlow = _refreshFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            _refreshFlow.emit(RefreshState())

            actions.collect {
                when(it is ClickAction) {
                    true -> avoidMultiTouch(it)
                    false -> handleActions(it)
                }
            }
        }
    }

    private suspend fun avoidMultiTouch(action: A) {
        if (System.currentTimeMillis().minus(lastClickEvent.value) > 300) {
            handleActions(action)
            lastClickEvent.value = System.currentTimeMillis()
        }
    }

    protected abstract suspend fun handleActions(action: A)

    protected fun setScreenState(reduce: S.() -> S) {
        _screenState.value = currentScreenState.reduce()
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun mockScreenState(reduce: S.() -> S) {
        _screenState.value = currentScreenState.reduce()
    }

    fun setAction(action: A) = viewModelScope.launch {
        _actions.emit(action)
    }

    protected fun setEffect(builder: () -> E) = viewModelScope.launch { _effect.send(builder()) }

    protected fun refresh(isForceRefresh: Boolean = false, isAutoRefresh: Boolean = false) =
        viewModelScope.launch {
            _refreshFlow.emit(
                RefreshState(isForceRefresh, isAutoRefresh)
            )
        }

}
