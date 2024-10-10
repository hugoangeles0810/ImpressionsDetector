package io.github.hugoangeles0810.impresssionsdetector

import androidx.annotation.FloatRange
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import io.github.hugoangeles0810.impresssionsdetector.util.FakeCurrentTime
import io.github.hugoangeles0810.impresssionsdetector.util.ImpressionCallbackSpy
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

class ImpressionDetectorTest {

    private val currentTime = FakeCurrentTime
    private val containerBounds = Rect(Offset.Zero, Size(CONTAINER_WIDTH, CONTAINER_HEIGHT))

    private lateinit var detector: ImpressionDetector

    @Before
    fun setUp() {
        detector = ImpressionDetector(
            currentTime = currentTime,
            areaCalculator = DefaultVisibleCalculator,
            rule = ImpressionRule()
        )
    }

    @After
    fun tearDown() {
        currentTime.reset()
    }

    @Test
    fun `item outside container bounds  after 1 sec should not be detected as impression`() {
        val impressionCallback = ImpressionCallbackSpy()

        detector.onItemLayoutCoordinatesChange(
            createRectWithVisibility(percentage = 0f),
            containerBounds,
            ITEM_KEY,
            impressionCallback
        )

        currentTime.advanceTimeBy(1.seconds)

        detector.onItemLayoutCoordinatesChange(
            createRectWithVisibility(0f),
            containerBounds,
            ITEM_KEY,
            impressionCallback
        )

        assertFalse(impressionCallback.wasCalled)
    }

    @Test
    fun `item inside container gets 50 percent visible then invisible after 1 sec should be detected as impression`() {
        val impressionCallback = ImpressionCallbackSpy()

        detector.onItemLayoutCoordinatesChange(
            createRectWithVisibility(0.5f),
            containerBounds,
            ITEM_KEY,
            impressionCallback
        )

        currentTime.advanceTimeBy(1.seconds)

        detector.onItemLayoutCoordinatesChange(
            createRectWithVisibility(0f),
            containerBounds,
            ITEM_KEY,
            impressionCallback
        )

        assertTrue(impressionCallback.wasCalled)
    }

    @Test
    fun `item inside container gets 40 percent visible then invisible after 1 sec should not be detected as impression`() {
        val impressionCallback = ImpressionCallbackSpy()

        detector.onItemLayoutCoordinatesChange(
            createRectWithVisibility(percentage = 0.4f),
            containerBounds,
            ITEM_KEY,
            impressionCallback
        )

        currentTime.advanceTimeBy(1.seconds)

        detector.onItemLayoutCoordinatesChange(
            createRectWithVisibility(percentage = 0f),
            containerBounds,
            ITEM_KEY,
            impressionCallback
        )

        assertFalse(impressionCallback.wasCalled)
    }

    @Test
    fun `item inside container gets 50 percent visible then invisible after 0_5 sec should not be detected as impression`() {
        val impressionCallback = ImpressionCallbackSpy()

        detector.onItemLayoutCoordinatesChange(
            createRectWithVisibility(percentage = 0.5f),
            containerBounds,
            ITEM_KEY,
            impressionCallback
        )

        currentTime.advanceTimeBy(0.5.seconds)

        detector.onItemLayoutCoordinatesChange(
            createRectWithVisibility(percentage = 0f),
            containerBounds,
            ITEM_KEY,
            impressionCallback
        )

        assertFalse(impressionCallback.wasCalled)
    }

    @Test
    fun `item inside container gets 50 percent visible then after 1 sec detached should be detected as impression`() {
        val impressionCallback = ImpressionCallbackSpy()

        detector.onItemLayoutCoordinatesChange(
            createRectWithVisibility(percentage = 0.5f),
            containerBounds,
            ITEM_KEY,
            impressionCallback
        )

        currentTime.advanceTimeBy(1.seconds)

        detector.onItemDisposed(key = ITEM_KEY)

        assertTrue(impressionCallback.wasCalled)
    }

    @Test
    fun `item inside container gets 50 percent visible then after 0_5 sec detached should not be detected as impression`() {
        val impressionCallback = ImpressionCallbackSpy()

        detector.onItemLayoutCoordinatesChange(
            createRectWithVisibility(percentage = 0.5f),
            containerBounds,
            ITEM_KEY,
            impressionCallback
        )

        currentTime.advanceTimeBy(0.5.seconds)

        detector.onItemDisposed(key = ITEM_KEY)

        assertFalse(impressionCallback.wasCalled)
    }

    private fun createRectWithVisibility(@FloatRange(from = 0.0, to = 1.0) percentage: Float): Rect {
        val visibleHeight = ITEM_HEIGHT * percentage
        val top = CONTAINER_HEIGHT - visibleHeight
        return Rect(
            left = 0f,
            right = ITEM_WIDTH,
            top = top,
            bottom = top + ITEM_HEIGHT
        )
    }

    private companion object {
        const val CONTAINER_WIDTH = 360f
        const val CONTAINER_HEIGHT = 800f

        const val ITEM_WIDTH = 180f
        const val ITEM_HEIGHT = 100f

        const val ITEM_KEY = "key"
    }
}