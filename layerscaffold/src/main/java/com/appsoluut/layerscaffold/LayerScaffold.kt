package com.appsoluut.layerscaffold

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.collapse
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.expand
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.offset
import com.appsoluut.layerscaffold.LayerScaffoldDefaults.FrontLayerHeaderElevation
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

@Composable
@ExperimentalMaterialApi
fun LayerScaffold(
    backLayerContent: @Composable () -> Unit,
    frontLayerContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit = {},
    scaffoldState: LayerScaffoldState = rememberLayerScaffoldState(initialValue = LayerValue.Revealed),
    gesturesEnabled: Boolean = true,
    headerHeight: Dp = LayerScaffoldDefaults.HeaderHeight,
    backLayerPeekHeight: Dp = LayerScaffoldDefaults.BackLayerPeekHeight,
    backLayerBackgroundColor: Color = MaterialTheme.colors.primary,
    backLayerContentColor: Color = contentColorFor(backLayerBackgroundColor),
    frontLayerPeekHeight: Dp? = null,
    frontLayerBackgroundColor: Color = MaterialTheme.colors.surface,
    frontLayerContentColor: Color = contentColorFor(frontLayerBackgroundColor),
    frontLayerHeaderElevation: Dp = FrontLayerHeaderElevation,
    frontLayerHeader: @Composable () -> Unit = {},
    frontLayerHandle: @Composable () -> Unit = { LayerHandle() },
) {
    val headerHeightPx: Float
    val backLayerPeekHeightPx: Float
    var frontLayerPeekHeightPx: Float
    with(LocalDensity.current) {
        headerHeightPx = headerHeight.toPx()
        backLayerPeekHeightPx = backLayerPeekHeight.toPx()
        frontLayerPeekHeightPx = frontLayerPeekHeight?.toPx() ?: -1f
    }

    val calculateBackLayerConstraints: (Constraints) -> Constraints = {
        it.copy(minWidth = 0, minHeight = 0).offset(vertical = -headerHeightPx.roundToInt())
    }

    // Back layer
    Surface(
        color = backLayerBackgroundColor,
        contentColor = backLayerContentColor
    ) {
        val scope = rememberCoroutineScope()
        Stack(
            modifier = modifier.fillMaxSize(),
            bottomBar = bottomBar,
            backLayer = backLayerContent,
            frontLayerHandle = frontLayerHandle,
            frontLayerHeader = frontLayerHeader,
            calculateBackLayerConstraints = calculateBackLayerConstraints
        ) { constraints, backLayerHeight, combinedHeaderHeight, bottomBarHeight ->
            val fullHeight = constraints.maxHeight.toFloat()
            val revealedHeight = fullHeight - headerHeightPx
            if (frontLayerPeekHeightPx < 0) {
                frontLayerPeekHeightPx = combinedHeaderHeight
            }
            val peekHeight = fullHeight - frontLayerPeekHeightPx - bottomBarHeight

            val concealedContentDescription = stringResource(id = R.string.layerscaffold_cd_concealed)
            val revealedContentDescription = stringResource(id = R.string.layerscaffold_cd_revealed)
            val peekingContentDescription = stringResource(id = R.string.layerscaffold_cd_peeking)

            val nestedScroll = if (gesturesEnabled) {
                Modifier.nestedScroll(scaffoldState.nestedScrollConnection)
            } else {
                Modifier
            }

            val swipeable = Modifier
                .then(nestedScroll)
                .swipeable(
                    state = scaffoldState,
                    anchors = mapOf(
                        backLayerPeekHeightPx to LayerValue.Concealed,
                        revealedHeight to LayerValue.Revealed,
                        peekHeight to LayerValue.Peeking,
                    ),
                    orientation = Orientation.Vertical,
                    enabled = gesturesEnabled
                )
                .semantics {
                    when (scaffoldState.currentValue) {
                        LayerValue.Concealed -> {
                            collapse {
                                if (scaffoldState.confirmStateChange(LayerValue.Revealed)) {
                                    scope.launch { scaffoldState.reveal() }
                                }
                                true
                            }
                            contentDescription = concealedContentDescription
                        }

                        LayerValue.Revealed -> {
                            expand {
                                if (scaffoldState.confirmStateChange(LayerValue.Concealed)) {
                                    scope.launch { scaffoldState.conceal() }
                                }
                                true
                            }
                            contentDescription = revealedContentDescription
                        }

                        LayerValue.Peeking -> {
                            expand {
                                if (scaffoldState.confirmStateChange(LayerValue.Revealed)) {
                                    scope.launch { scaffoldState.reveal() }
                                }
                                true
                            }
                            contentDescription = peekingContentDescription
                        }
                    }
                }

            // Front layer
            Surface(
                modifier = Modifier
                    .offset { IntOffset(0, scaffoldState.offset.value.roundToInt()) }
                    .then(swipeable),
                color = frontLayerBackgroundColor,
                contentColor = frontLayerContentColor
            ) {
                FrontLayer(
                    modifier = Modifier.padding(bottom = backLayerPeekHeight),
                    handle = frontLayerHandle,
                    header = frontLayerHeader,
                    content = frontLayerContent,
                    headerElevation = frontLayerHeaderElevation
                )
            }
        }
    }
}
