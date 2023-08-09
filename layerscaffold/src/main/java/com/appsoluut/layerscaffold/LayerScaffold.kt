package com.appsoluut.layerscaffold

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.semantics.collapse
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.expand
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.offset
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

@Composable
@ExperimentalMaterialApi
fun LayerScaffold(
    backLayerContent: @Composable () -> Unit,
    frontLayerContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    scaffoldState: LayerScaffoldState = rememberLayerScaffoldState(initialValue = LayerValue.Revealed),
    gesturesEnabled: Boolean = true,
    headerHeight: Dp = LayerScaffoldDefaults.HeaderHeight,
    backLayerPeekHeight: Dp = LayerScaffoldDefaults.PeekHeight,
    backLayerBackgroundColor: Color = MaterialTheme.colors.primary,
    backLayerContentColor: Color = contentColorFor(backLayerBackgroundColor),
    frontLayerPeekHeight: Dp = LayerScaffoldDefaults.PeekHeight,
    frontLayerBackgroundColor: Color = MaterialTheme.colors.surface,
    frontLayerContentColor: Color = contentColorFor(frontLayerBackgroundColor),
    frontLayerHandle: @Composable () -> Unit = { LayerHandle() }
) {
    val headerHeightPx: Float
    val backLayerPeekHeightPx: Float
    val frontLayerPeekHeightPx: Float
    with(LocalDensity.current) {
        headerHeightPx = headerHeight.toPx()
        backLayerPeekHeightPx = backLayerPeekHeight.toPx()
        frontLayerPeekHeightPx = frontLayerPeekHeight.toPx()
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
            backLayer = backLayerContent,
            calculateBackLayerConstraints = calculateBackLayerConstraints
        ) { constraints, backLayerHeight ->
            val fullHeight = constraints.maxHeight.toFloat()
            val revealedHeight = fullHeight - headerHeightPx
            val peekHeight = fullHeight - frontLayerPeekHeightPx

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
                                if (scaffoldState.confirmStateChange(LayerValue.Peeking)) {
                                    scope.launch { scaffoldState.peek() }
                                }
                                true
                            }
                            contentDescription = "Expand"
                        }
                        LayerValue.Revealed -> {
                            expand {
                                if (scaffoldState.confirmStateChange(LayerValue.Concealed)) {
                                    scope.launch { scaffoldState.conceal() }
                                }
                                true
                            }
                            contentDescription = "Collapse"
                        }
                        LayerValue.Peeking -> {
                            expand {
                                if (scaffoldState.confirmStateChange(LayerValue.Revealed)) {
                                    scope.launch { scaffoldState.reveal() }
                                }
                                true
                            }
                            contentDescription = "Peek"
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
                Box(modifier = Modifier.padding(bottom = backLayerPeekHeight)) {
                    frontLayerHandle()
                    frontLayerContent()
                }
            }
        }
    }
}
