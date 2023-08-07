package com.appsoluut.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.appsoluut.demo.ui.theme.LayerScaffoldTheme
import com.appsoluut.layerscaffold.LayerScaffold

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LayerScaffoldTheme {
                // A layer container using the 'background' color from the theme
                LayerScaffold(
                    modifier = Modifier.fillMaxSize(),
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
