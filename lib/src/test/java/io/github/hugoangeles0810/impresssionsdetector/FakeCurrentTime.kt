package io.github.hugoangeles0810.impresssionsdetector

import kotlin.time.Duration

object FakeCurrentTime : () -> Long {

    private var time: Long = 0

    fun advanceTimeBy(duration: Duration) {
        this.time += duration.inWholeMilliseconds
    }

    fun reset() {
        time = 0
    }

    override fun invoke() = time
}