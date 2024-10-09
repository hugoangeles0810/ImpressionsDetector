package io.github.hugoangeles0810.impresssionsdetector

import androidx.annotation.FloatRange
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
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
        val impressionSpy = ImpressionCallbackSpy()

        detector.onItemLayoutCoordinatesChange(
            rectWithVerticalVisibility(0f),
            containerBounds,
            ITEM_KEY,
            impressionSpy
        )

        currentTime.advanceTimeBy(1.seconds)

        detector.onItemLayoutCoordinatesChange(
            rectWithVerticalVisibility(0f),
            containerBounds,
            ITEM_KEY,
            impressionSpy
        )

        assertFalse(impressionSpy.wasCalled)
    }

    @Test
    fun `item in container gets 50 percent visible then invisible after 1 sec should be detected as impression`() {
        val impressionSpy = ImpressionCallbackSpy()

        detector.onItemLayoutCoordinatesChange(
            rectWithVerticalVisibility(0.5f),
            containerBounds,
            ITEM_KEY,
            impressionSpy
        )

        currentTime.advanceTimeBy(1.seconds)

        detector.onItemLayoutCoordinatesChange(
            rectWithVerticalVisibility(0f),
            containerBounds,
            ITEM_KEY,
            impressionSpy
        )

        assertTrue(impressionSpy.wasCalled)
    }

    @Test
    fun `item in container gets 40 percent visible then invisible after 1 sec should not be detected as impression`() {
        val impressionSpy = ImpressionCallbackSpy()

        detector.onItemLayoutCoordinatesChange(
            rectWithVerticalVisibility(0.4f),
            containerBounds,
            ITEM_KEY,
            impressionSpy
        )

        currentTime.advanceTimeBy(1.seconds)

        detector.onItemLayoutCoordinatesChange(
            rectWithVerticalVisibility(0f),
            containerBounds,
            ITEM_KEY,
            impressionSpy
        )

        assertFalse(impressionSpy.wasCalled)
    }

    @Test
    fun `item in container gets 50 percent visible then invisible after 0_5 sec should not be detected as impression`() {
        val impressionSpy = ImpressionCallbackSpy()

        detector.onItemLayoutCoordinatesChange(
            rectWithVerticalVisibility(0.5f),
            containerBounds,
            ITEM_KEY,
            impressionSpy
        )

        currentTime.advanceTimeBy(0.5.seconds)

        detector.onItemLayoutCoordinatesChange(
            rectWithVerticalVisibility(0f),
            containerBounds,
            ITEM_KEY,
            impressionSpy
        )

        assertFalse(impressionSpy.wasCalled)
    }

    private fun rectWithVerticalVisibility(@FloatRange(from = 0.0, to = 1.0) percentage: Float): Rect {
        val top = CONTAINER_HEIGHT - (ITEM_HEIGHT*percentage)
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