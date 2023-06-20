package com.austintech.intellijseniorflutter.uiComponents

import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.project.ProjectManager
import com.intellij.ui.components.JBPanel
import java.awt.BorderLayout

class DartSourceCodeEditor(dartCode: String) : JBPanel<DartSourceCodeEditor>(BorderLayout()) {
    private val editor: Editor
    init {
        val project = ProjectManager.getInstance().defaultProject
        val fileType = FileTypeManager.getInstance().getFileTypeByExtension("dart")
        val document: Document = EditorFactory.getInstance().createDocument(dartCode)
        editor = EditorFactory.getInstance().createEditor(document, project, fileType, false)

        add(editor.component, BorderLayout.CENTER)
    }

    fun getDartCode(): String {
        return editor.document.text
    }
}