package com.vml.tutorial.plantshop.core.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.roundToInt

data class DashedShape(
    val step: Dp
): Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density): Outline = Outline.Generic(Path().apply {
        val stepPx = with(density) { step.toPx() }
        val stepsCount = (size.height / stepPx).roundToInt()
        val actualStep = size.width / stepsCount
        val dotSize = Size(height = actualStep / 2, width = size.width)
        for (i in 0 until stepsCount) {
            addRect(
                Rect(
                    offset = Offset(y = i * actualStep, x = 0f),
                    size = dotSize
                )
            )
        }
        close()
    })
}