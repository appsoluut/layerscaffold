package com.appsoluut.layerscaffold

/**
 * Possible values of [LayerScaffoldState].
 */
enum class LayerValue {
    /**
     * Indicates the back layer is concealed and the front layer is active.
     */
    Concealed,

    /**
     * Indicates the back layer is revealed and the front layer is inactive.
     */
    Revealed,

    /**
     * Indicates the back layer is revealed and the front layer is peeking.
     */
    Peeking
}
