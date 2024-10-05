package io.github.hugoangeles0810.impresssionsdetector

import androidx.annotation.IntRange

class ImpressionRule(
    private val duration: Long = 1000,
    @IntRange(from = 0, to = 100)
    private val areaPercentage: Int = 50
) {

    fun isVisibleSufficient(percentage: Int) = percentage >= areaPercentage

    fun isDurationSufficient(timeAdded: Long, timeRemoved: Long) = timeRemoved - timeAdded >= duration
}