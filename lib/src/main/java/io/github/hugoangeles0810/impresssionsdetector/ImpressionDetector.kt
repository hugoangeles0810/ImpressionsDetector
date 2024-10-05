package io.github.hugoangeles0810.impresssionsdetector

import android.os.SystemClock

class ImpressionDetector internal constructor(
    private val currentTime: () -> Long,
    private val areaCalculator: VisibleAreaCalculator,
    private val rule: ImpressionRule
) {

    constructor(
        rule: ImpressionRule = ImpressionRule()
    ) : this(
        currentTime = { SystemClock.uptimeMillis() },
        areaCalculator = DefaultVisibleCalculator,
        rule = rule
    )
}