# LayerScaffold States

LayerScaffold supports the following 3 states:

1. `Concealed` - The back layer is not hidden (only the `backLayerPeekHeight` is still visible)
2. `Peeking` - The front layer is only showing it's `frontLayerPeekHeight` and the back layer is
   fully visible, it will accommodate for the bottom bar height automatically
3. `Revealed` - Both the back and front layers are visible (the `headerHeight` part is visible on the 
   back layer)

A state can also be changed programmatically:

```kotlin
val scaffoldState = rememberLayerScaffoldState()
val scope = rememberCoroutineScope()

LaunchedEffect(Unit) {
    scope.launch {
        scaffoldState.reveal()
    }
}
```
