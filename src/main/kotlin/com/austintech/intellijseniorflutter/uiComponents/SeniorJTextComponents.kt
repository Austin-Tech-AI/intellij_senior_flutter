package com.austintech.intellijseniorflutter.uiComponents

import com.intellij.ui.components.JBTextArea
import com.intellij.ui.components.JBTextField
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.geom.RoundRectangle2D
import javax.swing.BorderFactory
import javax.swing.text.JTextComponent

class SeniorJBTextArea(
    private val hint: String,
    private val paddingValue: Int = 8,
) : JBTextArea() {

    override fun paintComponent(g: Graphics) {
        val roundedGraphics = getRoundedCornersGraphic(g)
        super.paintComponent(roundedGraphics)
        border = getPaddedBorder()

        paintHintTextIfOkayToPaint(roundedGraphics, hint, paddingValue)
    }
}

class SeniorJBTextField(
    private val hint: String,
    private val paddingValue: Int = 8,
) : JBTextField() {

    override fun paintComponent(g: Graphics) {
        val roundedGraphics = getRoundedCornersGraphic(g)
        super.paintComponent(roundedGraphics)
        border = getPaddedBorder()

        paintHintTextIfOkayToPaint(roundedGraphics, hint, paddingValue)
    }
}

private fun getPaddedBorder(paddingValue: Int=10) = BorderFactory.createEmptyBorder(
    paddingValue,
    paddingValue,
    paddingValue,
    paddingValue
) // top, left, bottom, right

/**
 * Gets a graphics object with rounded corners.
 *
 * @param g the graphics object to paint on
 * @param radius the radius to use for the rounded corners
 * @return a graphics object with rounded corners
 */
private fun JTextComponent.getRoundedCornersGraphic(g: Graphics, radius: Double = 20.0): Graphics2D {
    val g2d = g.create() as Graphics2D
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    g2d.clip(RoundRectangle2D.Double(0.0, 0.0, width.toDouble(), height.toDouble(), radius, radius))
    return g2d
}

/**
 * Paints the hint text if it's okay to paint it.
 *
 * @param g the graphics object to paint on
 * @param hint the hint text to paint
 * @param paddingValue the padding value to use
 */
private fun JTextComponent.paintHintTextIfOkayToPaint(g: Graphics, hint: String, paddingValue: Int) {
    if (text.isEmpty() && !hasFocus()) {
        g.color = Color.GRAY
        val fm = g.fontMetrics
        g.drawString(hint, (paddingValue + 2), fm.ascent + (paddingValue + 4)) // Draw hint text
    }
}