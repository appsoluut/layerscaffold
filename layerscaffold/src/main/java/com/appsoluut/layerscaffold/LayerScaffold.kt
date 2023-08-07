package com.appsoluut.layerscaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Composable
fun LayerScaffold(
    backLayerContent: @Composable () -> Unit,
    frontLayerContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    backLayerBackgroundColor: Color = Color.LightGray,
    backLayerContentColor: Color = Color.Black,
) {
    val calculateBackLayerConstraints: (Constraints) -> Constraints = {
        it.copy(minWidth = 0, minHeight = 0)
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
            // Front layer
            Surface(
                modifier = Modifier.offset { IntOffset(0, 120) },
                color = Color.White
            ) {
                Box(modifier = Modifier.padding(bottom = 120.dp)) {
                    frontLayerContent()
                }
            }
        }
    }
}
