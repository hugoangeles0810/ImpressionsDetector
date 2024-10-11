package io.github.hugoangeles0810.impresssionsdetector

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.GlobalPositionAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.debugInspectorInfo

val LocalImpressionDetector =
    staticCompositionLocalOf<ImpressionDetector> { error("No ImpressionDetector found in Composition!") }

fun Modifier.impression(
    key: Any,
    onImpression: () -> Unit
): Modifier = this then ImpressionDetectorModifier(key, onImpression)

@Immutable
private data class ImpressionDetectorModifier(
    private val key: ImpressionKey,
    private val onImpression: ImpressionCallback
) : ModifierNodeElement<ImpressionNode>() {

    override fun InspectorInfo.inspectableProperties() {
        debugInspectorInfo {
            properties["key"] = key
        }
    }

    override fun create() = ImpressionNode(key, onImpression)

    override fun update(node: ImpressionNode) {
        node.key = key
        node.onImpression = onImpression
    }
}

private class ImpressionNode(
    var key: ImpressionKey,
    var onImpression: ImpressionCallback
) : Modifier.Node(),
    GlobalPositionAwareModifierNode,
    CompositionLocalConsumerModifierNode {

    private val impressionDetector: ImpressionDetector by lazy {
        currentValueOf(
            local = LocalImpressionDetector
        )
    }

    override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
        val view = currentValueOf(LocalView)

        if (coordinates.isAttached) {
            val containerRect = android.graphics.Rect()
            view.getGlobalVisibleRect(containerRect)

            impressionDetector.onItemLayoutCoordinatesChange(
                targetBounds = coordinates.boundsInWindow(),
                containerBounds = containerRect.toComposeRect(),
                key = key,
                onImpression = onImpression
            )
        } else {
            impressionDetector.onItemDisposed(key)
        }
    }

    override fun onDetach() {
        impressionDetector.onItemDisposed(key)
        super.onDetach()
    }
}
