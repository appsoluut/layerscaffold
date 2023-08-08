package com.appsoluut.layerscaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.offset
import kotlin.math.roundToInt

@Composable
@ExperimentalMaterialApi
fun LayerScaffold(
    backLayerContent: @Composable () -> Unit,
    frontLayerContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    scaffoldState: LayerScaffoldState = rememberLayerScaffoldState(initialValue = LayerValue.Revealed),
    peekHeight: Dp = LayerScaffoldDefaults.PeekHeight,
    headerHeight: Dp = LayerScaffoldDefaults.HeaderHeight,
    backLayerBackgroundColor: Color = MaterialTheme.colors.primary,
    backLayerContentColor: Color = contentColorFor(backLayerBackgroundColor),
    frontLayerBackgroundColor: Color = MaterialTheme.colors.surface,
    frontLayerContentColor: Color = contentColorFor(frontLayerBackgroundColor),
    frontLayerHandle: @Composable () -> Unit = { LayerHandle() }
) {
    val headerHeightPx: Float
    val peekHeightPx: Float
    with(LocalDensity.current) {
        headerHeightPx = headerHeight.toPx()
        peekHeightPx = peekHeight.toPx()
    }

    val calculateBackLayerConstraints: (Constraints) -> Constraints = {
        it.copy(minWidth = 0, minHeight = 0).offset(vertical = -headerHeightPx.roundToInt())
    }

    // Back layer
    Surface(
        color = backLayerBackgroundColor,
        contentColor = backLayerContentColor
    ) {
        Stack(
            modifier = modifier.fillMaxSize(),
            backLayer = backLayerContent,
            calculateBackLayerConstraints = calculateBackLayerConstraints
        ) { constraints, backLayerHeight ->
            val fullHeight = constraints.maxHeight.toFloat()
            //var revealedHeight = fullHeight - headerHeightPx

            // Front layer
            Surface(
                modifier = Modifier
                    .offset { IntOffset(0, scaffoldState.offset.value.roundToInt()) },
                color = frontLayerBackgroundColor,
                contentColor = frontLayerContentColor
            ) {
                Box(modifier = Modifier.padding(bottom = peekHeight)) {
                    frontLayerHandle()
                    frontLayerContent()
                }
            }
        }
    }
}
