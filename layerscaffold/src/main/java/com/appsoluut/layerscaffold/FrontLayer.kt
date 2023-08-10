package com.appsoluut.layerscaffold

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

/**
 * A composable that displays the front layer.
 *
 * @param handle The handle.
 * @param header The header.
 * @param content The content.
 * @param headerElevation The elevation of the header.
 * @param modifier The modifier to apply to this layout.
 */
@Composable
internal fun FrontLayer(
    handle: @Composable () -> Unit,
    header: @Composable () -> Unit,
    content: @Composable () -> Unit,
    headerElevation: Dp,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Surface(
            elevation = headerElevation
        ) {
            Column {
                handle()
                header()
            }
        }
        content()
    }
}
