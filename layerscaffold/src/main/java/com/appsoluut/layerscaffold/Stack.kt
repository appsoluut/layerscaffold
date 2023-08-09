package com.appsoluut.layerscaffold

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import kotlin.math.max

/**
 * A [Stack] composable that allows you to specify a [backLayer] and a [frontLayer].
 *
 * The [backLayer] is always placed at the back of the stack and the [frontLayer] is always
 * placed on top of the stack.
 *
 * @param modifier The modifier to apply to this layout.
 * @param backLayer The composable that will be placed at the back of the stack.
 * @param calculateBackLayerConstraints A function that will be called to calculate the constraints
 * of the [backLayer].
 * @param frontLayer The composable that will be placed on top of the stack.
 */
@Composable
@UiComposable
internal fun Stack(
    modifier: Modifier,
    backLayer: @Composable @UiComposable () -> Unit,
    calculateBackLayerConstraints: (Constraints) -> Constraints,
    frontLayer: @Composable @UiComposable (Constraints, Float) -> Unit
) {
    SubcomposeLayout(modifier) { constraints ->
        val backLayerPlaceable = subcompose(Layers.Back, backLayer)
            .first()
            .measure(calculateBackLayerConstraints(constraints))

        val backLayerHeight = backLayerPlaceable.height.toFloat()

        val placeables = subcompose(Layers.Front) {
            frontLayer(constraints, backLayerHeight)
        }.fastMap { it.measure(constraints) }

        var maxWidth = max(constraints.minWidth, backLayerPlaceable.width)
        var maxHeight = max(constraints.minHeight, backLayerPlaceable.height)
        placeables.fastForEach {
            maxWidth = max(maxWidth, it.width)
            maxHeight = max(maxHeight, it.height)
        }

        layout(maxWidth, maxHeight) {
            backLayerPlaceable.placeRelative(0, 0)
            placeables.fastForEach { it.placeRelative(0, 0) }
        }
    }
}
