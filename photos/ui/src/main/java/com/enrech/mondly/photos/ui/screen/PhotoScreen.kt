package com.enrech.mondly.photos.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.enrech.mondly.core.domain.exception.NoInternetException
import com.enrech.mondly.design_system.theme.MondlyTheme
import com.enrech.mondly.photos.domain.entity.PhotoEntity
import com.enrech.mondly.photos.ui.R
import com.enrech.mondly.photos.ui.components.ItemCard
import com.enrech.mondly.photos.ui.components.ItemCardPlaceholder
import com.enrech.mondly.viewmodel_util.HandleEffects
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import com.enrech.mondly.design_system.R as DesignR

@RootNavGraph(start = true)
@Destination
@Composable
fun PhotoScreen(
    navigator: DestinationsNavigator,
    viewModel: PhotosViewModel = hiltViewModel()
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val noNetworkError = stringResource(id = DesignR.string.secondary_network_error)
    val generalError = stringResource(id = DesignR.string.secondary_general_error)
    val tryAgain = stringResource(id = DesignR.string.try_again)

    HandleEffects(viewModel.effect) {
        when(it) {
            is PhotoEffect.OnSecondaryError -> {
                val message = if (it.isNetworkError) noNetworkError else generalError
                launch {
                    val result = snackbarHostState.showSnackbar(
                        message = message,
                        actionLabel = tryAgain,
                        duration = SnackbarDuration.Long
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.setAction(PhotoAction.Refresh)
                    }
                }
            }
        }
    }

    UIContent(state, snackbarHostState, viewModel::setAction)
}

@Composable
private fun UIContent(
    state: PhotosState,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    action: (PhotoAction) -> Unit
) {
    val pullToRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = { action(PhotoAction.Refresh) }
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceDim)
                .pullRefresh(pullToRefreshState, !state.isInitializing)
        )
        {
            when {
                state.error != null && !state.isInitializing -> ErrorScreen(throwable = state.error)
                state.error == null && !state.isInitializing && state.data.isEmpty() -> EmptyScreen()
                else -> ValidContent(state = state)
            }

            PullRefreshIndicator(
                refreshing = state.isRefreshing,
                state = pullToRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
private fun BoxScope.ValidContent(state: PhotosState) {
    LazyColumn(
        modifier = Modifier.matchParentSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        userScrollEnabled = !state.isInitializing,
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        if (state.isInitializing) {
            items(8) {
                ItemCardPlaceholder()
            }
        } else {
            items(state.data, key = { it.id }) {
                ItemCard(data = it)
            }
        }
    }
}

@Composable
private fun BoxScope.EmptyScreen() {
    Column(
        Modifier
            .matchParentSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 48.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(150.dp),
            painter = painterResource(R.drawable.ic_no_photos),
            contentDescription = stringResource(R.string.no_photos_cd),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(id = R.string.no_images),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun BoxScope.ErrorScreen(throwable: Throwable) {
    val iconRes by remember {
        derivedStateOf {
            when {
                throwable is NoInternetException -> DesignR.drawable.ic_no_internet
                else -> DesignR.drawable.ic_default_error
            }
        }
    }

    val textRes by remember {
        derivedStateOf {
            when {
                throwable is NoInternetException -> DesignR.string.network_error
                else -> DesignR.string.general_error
            }
        }
    }

    Column(
        Modifier
            .matchParentSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 48.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(150.dp),
            painter = painterResource(iconRes),
            contentDescription = stringResource(DesignR.string.error_cd),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(id = textRes),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun PhotoScreenLoadingPreview() {
    MondlyTheme {
        UIContent(
            state = PhotosState()
        ) {}
    }
}

@Preview
@Composable
private fun PhotoScreenNetworkErrorPreview() {
    MondlyTheme {
        UIContent(
            state = PhotosState(
                isInitializing = false,
                error = NoInternetException()
            )
        ) {}
    }
}

@Preview
@Composable
private fun PhotoScreenGeneralErrorPreview() {
    MondlyTheme {
        UIContent(
            state = PhotosState(
                isInitializing = false,
                error = IllegalStateException()
            )
        ) {}
    }
}

@Preview
@Composable
private fun PhotoScreenValidPreview() {
    MondlyTheme {
        UIContent(
            state = PhotosState(
                isInitializing = false,
                data = listOf(
                    PhotoEntity(
                        id = 1,
                        name = "Some image name",
                        description = "Some image description",
                        ""
                    )
                )
            )
        ) {}
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun PhotoScreenEmtpyPreview() {
    MondlyTheme {
        UIContent(
            state = PhotosState(isInitializing = false)
        ) {}
    }
}