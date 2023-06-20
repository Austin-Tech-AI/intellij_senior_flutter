package com.austintech.intellijseniorflutter.uiComponents

import com.austintech.intellijseniorflutter.MyBundle
import com.austintech.intellijseniorflutter.services.EditCodeService
import com.austintech.intellijseniorflutter.services.SelectedFileService
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import javax.swing.JButton

class RequestChangesButton(project: Project, changesTextArea: SeniorJBTextArea, importsTextField: SeniorJBTextField) {
    private val selectedFileService = project.service<SelectedFileService>()
    private val editCodeService = project.service<EditCodeService>()

    val button: JButton = JButton(MyBundle.message("make_changes")).apply {
        addActionListener {
            if (selectedFileService.currentlySelectedFile == null) {
                return@addActionListener
            }

            editCodeService.sendEditTask(
                selectedFileService.currentlySelectedFile!!,
                changesTextArea.text,
                importsTextField.text
            )
        }
    }
}