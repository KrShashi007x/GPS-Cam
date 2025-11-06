package com.krshashi.gpscam.app.ui.getstarted

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.krshashi.gpscam.app.ui.getstarted.view.GetStartedView
import com.krshashi.gpscam.app.ui.getstarted.view.NoPermissionView
import com.krshashi.gpscam.app.ui.getstarted.view.PermissionView
import com.krshashi.gpscam.app.utils.rememberAppPermissionsState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GetStartedScreen(
    onNavigateUp: () -> Unit,
    onNavigateToCamera: () -> Unit,
    onNavToAppSetting: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GetStartedViewModel = hiltViewModel()
) {
    val appPermissionsState = rememberAppPermissionsState()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })
    var isInitialRequest by remember { mutableStateOf(true) }

    when {
        appPermissionsState.allPermissionsGranted -> {
            LaunchedEffect(Unit) {
                onNavigateToCamera()
            }
        }

        !appPermissionsState.shouldShowRationale -> {
            LaunchedEffect(Unit) {
                if (isInitialRequest) {
                    pagerState.animateScrollToPage(0)
                } else {
                    pagerState.animateScrollToPage(2)
                }
            }
        }

        else -> {
            LaunchedEffect(Unit) {
                pagerState.animateScrollToPage(0)
            }
        }
    }

    GetStartedContents(
        pagerState = pagerState,
        permissionsState = appPermissionsState,
        modifier = modifier,
        onNavToAppSetting = onNavToAppSetting,
        onNavigateToCamera = onNavigateToCamera,
        onRequestPermission = {
            appPermissionsState.launchMultiplePermissionRequest()
            isInitialRequest = false
        }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun GetStartedContents(
    pagerState: PagerState,
    permissionsState: MultiplePermissionsState,
    modifier: Modifier = Modifier,
    onNavToAppSetting: () -> Unit,
    onRequestPermission: () -> Unit,
    onNavigateToCamera: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    HorizontalPager(state = pagerState, userScrollEnabled = false) {
        when (pagerState.currentPage) {
            1 -> PermissionView(
                modifier = modifier,
                onAllowRequest = onRequestPermission
            )

            2 -> NoPermissionView(
                modifier = modifier,
                onNavToAppSetting = onNavToAppSetting
            )

            else -> GetStartedView(modifier) {
                scope.launch {
                    when {
                        !permissionsState.allPermissionsGranted -> {
                            pagerState.animateScrollToPage(1)
                        }

                        !permissionsState.shouldShowRationale -> {
                            pagerState.animateScrollToPage(2)
                        }

                        else -> onNavigateToCamera()
                    }
                }
            }
        }
    }
}