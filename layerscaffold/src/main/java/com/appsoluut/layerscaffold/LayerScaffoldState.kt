package com.appsoluut.layerscaffold

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.SwipeableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CancellationException

/**
 * State of the [LayerScaffold] composable.
 *
 * @param initialValue The initial value of the state.
 * @param animationSpec The default animation that will be used to animate to a new state.
 * @property confirmStateChange Optional callback invoked to confirm or veto a pending state change.
 * @property snackbarHostState The [SnackbarHostState] used to show snackbars inside the scaffold.
 */
@ExperimentalMaterialApi
@Stable
class LayerScaffoldState(
    initialValue: LayerValue,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
    val confirmStateChange: (LayerValue) -> Boolean = { true },
    val snackbarHostState: SnackbarHostState = SnackbarHostState()
) : SwipeableState<LayerValue>(
    initialValue = initialValue,
    animationSpec = animationSpec,
    confirmStateChange = confirmStateChange
) {
    /**
     * Whether the back layer is revealed.
     */
    val isRevealed: Boolean
        get() = currentValue != LayerValue.Concealed

    /**
     * Whether the back layer is concealed.
     */
    val isConcealed: Boolean
        get() = currentValue == LayerValue.Concealed

    /**
     * Whether the front layer is peeking.
     */
    val isPeeking: Boolean
        get() = currentValue == LayerValue.Peeking

    internal val nestedScrollConnection = this.PreUpPostDownNestedScrollConnection

    /**
     * Reveal the back layer with animation and suspend until it if fully revealed or animation
     * has been cancelled.  This method will throw [CancellationException] if the animation is
     * interrupted
     *
     * @return the reason the reveal animation ended
     */
    suspend fun reveal() = animateTo(targetValue = LayerValue.Revealed)

    /**
     * Conceal the back layer with animation and suspend until it if fully concealed or animation
     * has been cancelled. This method will throw [CancellationException] if the animation is
     * interrupted
     *
     * @return the reason the conceal animation ended
     */
    suspend fun conceal() = animateTo(targetValue = LayerValue.Concealed)

    /**
     * Reveal the back layer with the front layer to peek height with animation and suspend until
     * it if fully revealed or animation has been cancelled. This method will throw
     * [CancellationException] if the animation is interrupted
     *
     * @return the reason the reveal animation ended
     */
    suspend fun peek() = animateTo(targetValue = LayerValue.Peeking)

    companion object {
        /**
         * The default [Saver] implementation for [LayerScaffoldState].
         */
        fun Saver(
            animationSpec: AnimationSpec<Float>,
            confirmStateChange: (LayerValue) -> Boolean,
            snackbarHostState: SnackbarHostState
        ): Saver<LayerScaffoldState, *> = Saver(
            save = { it.currentValue },
            restore = {
                LayerScaffoldState(
                    initialValue = it,
                    animationSpec = animationSpec,
                    confirmStateChange = confirmStateChange,
                    snackbarHostState = snackbarHostState
                )
            }
        )
    }
}

/**
 * Create and [remember] a [LayerScaffoldState].
 *
 * @param initialValue The initial value of the state.
 * @param animationSpec The default animation that will be used to animate to a new state.
 * @param confirmStateChange Optional callback invoked to confirm or veto a pending state change.
 * @param snackbarHostState The [SnackbarHostState] used to show snackbars inside the scaffold.
 */
@Composable
@ExperimentalMaterialApi
fun rememberLayerScaffoldState(
    initialValue: LayerValue,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
    confirmStateChange: (LayerValue) -> Boolean = { true },
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
): LayerScaffoldState {
    return rememberSaveable(
        animationSpec,
        confirmStateChange,
        snackbarHostState,
        saver = LayerScaffoldState.Saver(
            animationSpec = animationSpec,
            confirmStateChange = confirmStateChange,
            snackbarHostState = snackbarHostState
        )
    ) {
        LayerScaffoldState(
            initialValue = initialValue,
            animationSpec = animationSpec,
            confirmStateChange = confirmStateChange,
            snackbarHostState = snackbarHostState
        )
    }
}

// temp default nested scroll connection for swipeables which desire as an opt in
// revisit in b/174756744 as all types will have their own specific connection probably
@ExperimentalMaterialApi
internal val <T> SwipeableState<T>.PreUpPostDownNestedScrollConnection: NestedScrollConnection
    get() = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            val delta = available.toFloat()
            return if (delta < 0 && source == NestedScrollSource.Drag) {
                performDrag(delta).toOffset()
            } else {
                Offset.Zero
            }
        }

        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource
        ): Offset {
            return if (source == NestedScrollSource.Drag) {
                performDrag(available.toFloat()).toOffset()
            } else {
                Offset.Zero
            }
        }

        override suspend fun onPreFling(available: Velocity): Velocity {
            val toFling = Offset(available.x, available.y).toFloat()
            return if (toFling < 0 && offset.value > minBound) {
                performFling(velocity = toFling)
                // since we go to the anchor with tween settling, consume all for the best UX
                available
            } else {
                Velocity.Zero
            }
        }

        override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
            performFling(velocity = Offset(available.x, available.y).toFloat())
            return available
        }

        private fun Float.toOffset(): Offset = Offset(0f, this)

        private fun Offset.toFloat(): Float = this.y
    }

internal var minBound = Float.NEGATIVE_INFINITY
