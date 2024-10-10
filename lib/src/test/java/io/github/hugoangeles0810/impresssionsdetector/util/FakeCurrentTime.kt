package io.github.hugoangeles0810.impresssionsdetector.util

import kotlin.time.Duration

object FakeCurrentTime : () -> Long {

    private var time: Long = 0

    fun advanceTimeBy(duration: Duration) {
        time += duration.inWholeMilliseconds
    }

    fun reset() {
        time = 0
    }

    override fun invoke() = time
}