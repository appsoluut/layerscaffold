# LayerScaffold

`LayerScaffold` is a library that allows you to create a layered UI while using Jetpack Compose and
still be able to have an app bar, bottom navigation bar and bottom sheet. It's a simple solution to
a problem that is not yet solved in the Material Design Components library.

```groovy
implementation "com.appsoluut.layerscaffold:layerscaffold:1.0.0"
```

=== "Compose"
    ```kotlin
    LayerScaffold(
        frontLayerContent = {
            Text("Content as a supplement on top of your main content")
        },
    ) {
        Text("Your main content")
    }
    ```

* [Random Thoughts](https://hameteman.com)
* [AppSoluut](https://appsoluut.app)
