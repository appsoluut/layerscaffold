package com.appsoluut.layerscaffold

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LayerScaffold(
    modifier: Modifier = Modifier,
    frontLayerContent: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit
) {

}
