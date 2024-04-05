package com.enrech.mondly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.enrech.mondly.design_system.theme.MondlyTheme
import com.enrech.mondly.main.ui.screen.MainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MondlyTheme {
                MainScreen()
            }
        }
    }
}