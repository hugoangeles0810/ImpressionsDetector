package io.github.hugoangeles0810.impresssionsdetector.util

import io.github.hugoangeles0810.impresssionsdetector.ImpressionCallback

class ImpressionCallbackSpy : ImpressionCallback {

    private var invocations: Int = 0

    val wasCalled: Boolean
        get() = invocations > 0

    override fun invoke() {
        invocations++
    }
}