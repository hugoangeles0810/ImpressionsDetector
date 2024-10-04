package io.github.hugoangeles0810.impresssionsdetector

import androidx.annotation.IntRange
import androidx.compose.ui.geometry.Rect

internal interface VisibleAreaCalculator {

    @IntRange(from = 0, to = 100)
    fun visibleAreaPercentage(
        targetBounds: Rect,
        containerBounds: Rect
    ): Int
}

internal object DefaultVisibleCalculator : VisibleAreaCalculator {

    override fun visibleAreaPercentage(
        targetBounds: Rect,
        containerBounds: Rect
    ): Int {
        val targetArea = targetBounds.size.run {  width * height }.toInt()
        if (targetArea == 0) return 0

        val visibleRect = containerBounds.intersect(targetBounds)
        if (visibleRect.isEmpty) return 0

        val visibleArea = visibleRect.run { width * height }

        return ((visibleArea/targetArea) * 100).toInt()
    }
}