package com.austintech.intellijseniorflutter.uiComponents

import com.intellij.ui.components.JBPanel
import com.intellij.util.ui.JBUI

class SizedBox(width: Int = 0, height: Int = 0) : JBPanel<JBPanel<*>>() {

    companion object {
        fun hSmall() = SizedBox(height = 4)
    }

    init {
        preferredSize = JBUI.size(width, height)
    }
}