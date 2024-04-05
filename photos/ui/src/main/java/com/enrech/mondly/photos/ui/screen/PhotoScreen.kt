package com.enrech.mondly.photos.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.enrech.mondly.design_system.theme.MondlyTheme
import com.enrech.mondly.viewmodel_util.HandleEffects
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination
@Composable
fun PhotoScreen(
    navigator: DestinationsNavigator,
    viewModel: PhotosViewModel = hiltViewModel()
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()

    HandleEffects(viewModel.effect) {
        when(it) {
            else -> {}
        }
    }

    UIContent(state, viewModel::setAction)
}

@Composable
private fun UIContent(state: PhotosState, action: (PhotoAction) -> Unit) {

}

@Preview
@Composable
private fun PhotoScreenPreview() {
    MondlyTheme {
        UIContent(state = PhotosState()) {}
    }
}