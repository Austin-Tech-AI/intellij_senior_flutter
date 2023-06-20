package com.austintech.intellijseniorflutter.uiComponents

import com.intellij.ui.components.JBPanel
import org.jdesktop.swingx.VerticalLayout
import javax.swing.BorderFactory
import javax.swing.JProgressBar

class LoadingUI : JBPanel<JBPanel<*>>() {
    init {
        layout = VerticalLayout()
        border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

        val progressBar = JProgressBar(0, 100)
        progressBar.isIndeterminate = true
        add(progressBar)
    }
}