package io.github.hugoangeles0810.impresssionsdetector

import androidx.compose.ui.geometry.Rect
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DefaultVisibleCalculatorTest {

    private lateinit var calculator: VisibleAreaCalculator

    @Before
    fun setUp() {
        calculator = DefaultVisibleCalculator
    }

    @Test
    fun `given a target bounds outside of the container bounds should return zero percentage`() {
        val percentage = calculator.visibleAreaPercentage(
            targetBounds = Rect(0f, 800f, 100f, 900f),
            containerBounds = containerBounds
        )

        assertEquals(0, percentage)
    }

    @Test
    fun `given an empty target bounds should return zero percentage`() {
        val percentage = calculator.visibleAreaPercentage(
            targetBounds = Rect(0f, 100f, 0f, 100f),
            containerBounds = containerBounds
        )

        assertEquals(0, percentage)
    }

    @Test
    fun `given a fully visible target bounds should return 100 percentage`() {
        val percentage = calculator.visibleAreaPercentage(
            targetBounds = Rect(0f, 0f, 100f, 100f),
            containerBounds = containerBounds
        )

        assertEquals(100, percentage)
    }

    @Test
    fun `given partially visible target bounds should return expected percentage`() {
        val percentage = calculator.visibleAreaPercentage(
            targetBounds = Rect(310f, 0f, 410f, 100f),
            containerBounds = containerBounds
        )

        assertEquals(50, percentage)
    }

    companion object {
        private val containerBounds = Rect(0f, 0f, 360f, 800f)
    }
}