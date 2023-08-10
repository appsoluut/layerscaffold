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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appsoluut.demo.ui.BottomBar
import com.appsoluut.demo.ui.theme.LayerScaffoldTheme
import com.appsoluut.layerscaffold.LayerScaffold
import com.appsoluut.layerscaffold.LayerScaffoldDefaults
import com.appsoluut.layerscaffold.LayerValue
import com.appsoluut.layerscaffold.rememberLayerScaffoldState

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val headerHeight = LocalConfiguration.current.screenHeightDp / 2
            val scaffoldState = rememberLayerScaffoldState(initialValue = LayerValue.Revealed)
            val headerElevation by remember(scaffoldState.isConcealed) {
                derivedStateOf {
                    if (scaffoldState.isConcealed) {
                        LayerScaffoldDefaults.FrontLayerHeaderElevation
                    } else {
                        0.dp
                    }
                }
            }
            LayerScaffoldTheme {
                // A layer container using the 'background' color from the theme
                LayerScaffold(
                    scaffoldState = scaffoldState,
                    headerHeight = headerHeight.dp,
                    bottomBar = { BottomBar() },
                    backLayerContent = {
                        Greeting("Android")
                    },
                    frontLayerHeaderElevation = headerElevation,
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

/**
 * A composable that displays a header with a title and subtitle.
 *
 * @param modifier The modifier to apply to this layout.
 */
@Composable
fun SheetHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.sheet_header_title),
            style = MaterialTheme.typography.h6,
        )
        Text(
            text = stringResource(id = R.string.sheet_header_subtitle),
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
