package com.appsoluut.layerscaffold

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.appsoluut.layerscaffold.LayerScaffoldDefaults.FrontLayerHeaderElevation

@Composable
internal fun FrontLayer(
    handle: @Composable () -> Unit,
    header: @Composable () -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Surface(
            elevation = FrontLayerHeaderElevation
        ) {
            Column {
                handle()
                header()
            }
        }
        content()
    }
}
