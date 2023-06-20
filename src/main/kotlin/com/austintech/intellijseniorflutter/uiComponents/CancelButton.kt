package com.austintech.intellijseniorflutter.uiComponents

import com.austintech.intellijseniorflutter.MyBundle
import com.austintech.intellijseniorflutter.services.EditCodeService
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import javax.swing.JButton

class CancelButton(project: Project) : JButton() {
    private val editCodeService = project.service<EditCodeService>()

    init {
        text = MyBundle.message("cancel")
        addActionListener {
            editCodeService.cancel()
        }
    }
}
