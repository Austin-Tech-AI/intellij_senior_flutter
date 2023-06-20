package com.austintech.intellijseniorflutter.uiComponents

import com.intellij.ui.components.JBPanel
import org.jdesktop.swingx.VerticalLayout
import javax.swing.JLabel
import javax.swing.text.JTextComponent

class TitledTextComponent(title: String, textComponent: JTextComponent) : JBPanel<JBPanel<*>>() {

    init {
        layout = VerticalLayout()
        add(SizedBox.hSmall())
        add(JLabel(title))
        add(SizedBox.hSmall())
        add(textComponent)
    }

}