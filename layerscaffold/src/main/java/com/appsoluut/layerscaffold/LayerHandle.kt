package com.appsoluut.layerscaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.appsoluut.layerscaffold.LayerScaffoldDefaults.FrontLayerHandleHeight
import com.appsoluut.layerscaffold.LayerScaffoldDefaults.FrontLayerHandleMinHeight
import com.appsoluut.layerscaffold.LayerScaffoldDefaults.FrontLayerHandleWidth

/**
 * A composable that displays a handle for the front layer.
 *
 * @param modifier The modifier to apply to this layout.
 * @param handleModifier The modifier to apply to the handle.
 * @param contentAlignment The alignment of the handle.
 * @param color The color of the handle.
 * @param shape The shape of the handle.
 */
@Composable
internal fun LayerHandle(
    modifier: Modifier = Modifier,
    handleModifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    color: Color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f),
    shape: Shape = RoundedCornerShape(percent = 50)
) {
    Box(
        modifier = modifier.fillMaxWidth().defaultMinSize(minHeight = FrontLayerHandleMinHeight),
        contentAlignment = contentAlignment
    ) {
        Box(
            modifier = handleModifier
                .defaultMinSize(
                    minWidth = FrontLayerHandleWidth,
                    minHeight = FrontLayerHandleHeight
                )
                .background(color = color, shape = shape)
        )
    }
}
