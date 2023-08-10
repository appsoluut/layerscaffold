package com.appsoluut.layerscaffold

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMaxBy
import kotlin.math.max

/**
 * A [Stack] composable that allows you to specify a [backLayer] and a [frontLayer].
 *
 * The [backLayer] is always placed at the back of the stack and the [frontLayer] is always
 * placed on top of the stack.
 *
 * @param modifier The modifier to apply to this layout.
 * @param bottomBar The composable that will be placed at the bottom of the screen.
 * @param backLayer The composable that will be placed at the back of the stack.
 * @param calculateBackLayerConstraints A function that will be called to calculate the constraints
 * of the [backLayer].
 * @param frontLayer The composable that will be placed on top of the stack.
 */
@Composable
@UiComposable
internal fun Stack(
    modifier: Modifier,
    bottomBar: @Composable @UiComposable () -> Unit,
    backLayer: @Composable @UiComposable () -> Unit,
    calculateBackLayerConstraints: (Constraints) -> Constraints,
    frontLayerHandle: @Composable @UiComposable () -> Unit,
    frontLayerHeader: @Composable @UiComposable () -> Unit,
    frontLayer: @Composable @UiComposable (Constraints, Float, Float, Float) -> Unit
) {
    SubcomposeLayout(modifier) { constraints ->
        val looseConstraints = constraints.copy(
            minWidth = 0,
            minHeight = 0
        )

        val bottomBarPlaceables = subcompose(Layers.BottomBar, bottomBar)
            .fastMap { it.measure(looseConstraints) }

        val bottomBarHeight = bottomBarPlaceables.fastMaxBy { it.height }?.height ?: 0

        val backLayerPlaceable = subcompose(Layers.Back, backLayer)
            .first()
            .measure(calculateBackLayerConstraints(constraints))

        val backLayerHeight = backLayerPlaceable.height.toFloat()

        val frontLayerCombinedHeaderHeight = subcompose("header") {
            Column {
                frontLayerHandle()
                frontLayerHeader()
            }
        }.sumOf { it.measure(looseConstraints).height }.toFloat()

        val placeables = subcompose(Layers.Front) {
            frontLayer(constraints, backLayerHeight, frontLayerCombinedHeaderHeight, bottomBarHeight.toFloat())
        }.fastMap { it.measure(constraints) }

        var maxWidth = max(constraints.minWidth, backLayerPlaceable.width)
        var maxHeight = max(constraints.minHeight, backLayerPlaceable.height)
        placeables.fastForEach {
            maxWidth = max(maxWidth, it.width)
            maxHeight = max(maxHeight - bottomBarHeight, it.height)
        }

        layout(maxWidth, maxHeight) {
            backLayerPlaceable.placeRelative(0, 0)
            placeables.fastForEach { it.placeRelative(0, 0) }

            // The bottom bar is always at the bottom of the layout
            bottomBarPlaceables.fastForEach {
                it.place(0, maxHeight - bottomBarHeight)
            }
        }
    }
}
