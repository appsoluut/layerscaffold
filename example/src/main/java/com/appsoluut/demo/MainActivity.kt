package com.appsoluut.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appsoluut.demo.ui.theme.LayerScaffoldTheme
import com.appsoluut.layerscaffold.LayerScaffold

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val headerHeight = LocalConfiguration.current.screenHeightDp / 2
            LayerScaffoldTheme {
                // A layer container using the 'background' color from the theme
                LayerScaffold(
                    backLayerPeekHeight = 20.dp,
                    frontLayerPeekHeight = 100.dp,
                    headerHeight = headerHeight.dp,
                    backLayerContent = {
                        Greeting("Android")
                    },
                    frontLayerContent = {
                        Text("Front layer content")
                    }
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LayerScaffoldTheme {
        Greeting("Android")
    }
}
