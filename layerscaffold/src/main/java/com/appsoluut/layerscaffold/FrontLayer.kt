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
 * @param modifier The modifier to apply to this layout.
 * @param headerElevation The elevation of the header.
 */
@Composable
internal fun FrontLayer(
    handle: @Composable () -> Unit,
    header: @Composable () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    headerElevation: Dp
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
