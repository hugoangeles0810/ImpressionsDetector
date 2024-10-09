package io.github.hugoangeles0810.impresssionsdetector

import android.os.SystemClock
import androidx.compose.ui.geometry.Rect
import java.util.concurrent.ConcurrentHashMap

private typealias Key = Any
private typealias Value = Pair<Long, ImpressionCallback>
internal typealias ImpressionCallback = () -> Unit

class ImpressionDetector internal constructor(
    private val currentTime: () -> Long,
    private val areaCalculator: VisibleAreaCalculator,
    private val rule: ImpressionRule
) {

    private val visibleItems: MutableMap<Key, Value> by lazy { ConcurrentHashMap() }

    constructor(
        rule: ImpressionRule = ImpressionRule()
    ) : this(
        currentTime = { SystemClock.uptimeMillis() },
        areaCalculator = DefaultVisibleCalculator,
        rule = rule
    )

    internal fun onItemLayoutCoordinatesChange(
        targetBounds: Rect,
        containerBounds: Rect,
        key: Key,
        onImpression: ImpressionCallback
    ) {

        val visibleAreaPercentage =
            areaCalculator.visibleAreaPercentage(targetBounds, containerBounds)

        if (rule.isVisibleSufficient(visibleAreaPercentage)) {
            visibleItems.addIfNotExists(key, currentTime() to onImpression)
        } else {
            sendImpressionIfDurationSufficient(key)
        }

    }

    private fun sendImpressionIfDurationSufficient(
        key: Key,
        removeAfter: Boolean = true
    ) {
        visibleItems[key]?.let { (timeAdded, callback) ->
            if (rule.isDurationSufficient(timeAdded, currentTime())) {
                callback()
            }

            if (removeAfter) {
                visibleItems.remove(key)
            }
        }
    }

    private fun MutableMap<Key, Value>.addIfNotExists(key: Key, value: Value) {
        if (containsKey(key).not()) {
            set(key, value)
        }
    }
}