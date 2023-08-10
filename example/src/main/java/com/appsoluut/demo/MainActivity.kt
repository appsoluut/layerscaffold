package com.appsoluut.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appsoluut.demo.ui.BottomBar
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
                    headerHeight = headerHeight.dp,
                    bottomBar = { BottomBar() },
                    backLayerContent = {
                        Greeting("Android")
                    },
                    frontLayerHeader = {
                        SheetHeader()
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
fun SheetHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp).padding(bottom = 8.dp)
    ) {
        Text(
            text = "Header",
            style = MaterialTheme.typography.h6
        )
        Text(
            text = "Subtitle",
            style = MaterialTheme.typography.subtitle1
        )
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
