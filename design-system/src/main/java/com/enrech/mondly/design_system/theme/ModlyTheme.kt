package com.enrech.mondly.design_system.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun MondlyTheme(
    isInDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (isInDarkTheme) mondlyDarkColorScheme else mondlyLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}