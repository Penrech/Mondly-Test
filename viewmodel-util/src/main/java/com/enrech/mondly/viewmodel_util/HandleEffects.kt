package com.enrech.mondly.viewmodel_util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.enrech.mondly.shared_types.Effect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

@Composable
fun <T : Effect> HandleEffects(
    effectFlow: Flow<T>,
    key: Any? = Unit,
    content: CoroutineScope.(effect: T) -> Unit
) {
    LaunchedEffect(key1 = key) {
        effectFlow.collect { this.content(it) }
    }
}