package com.austintech.intellijseniorflutter.uiComponents

import com.austintech.intellijseniorflutter.MyBundle
import com.austintech.intellijseniorflutter.services.EditCodeService
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.JButton

class SaveChangesButton(
    project: Project,
    contents: String,
    fileToSaveContents: VirtualFile,
    changesTextArea: SeniorJBTextArea,
    importsTextField: SeniorJBTextField,
) : JButton(){
    private val editCodeService = project.service<EditCodeService>()

    init {
        text = MyBundle.message("save_changes")
        addActionListener {
            ApplicationManager.getApplication().runWriteAction {
                fileToSaveContents.setBinaryContent(contents.toByteArray())
            }
            changesTextArea.text = ""
            importsTextField.text = ""
            editCodeService.markAsSaved()
        }
    }
}