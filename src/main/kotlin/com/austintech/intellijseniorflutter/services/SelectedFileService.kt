package com.austintech.intellijseniorflutter.services

import com.austintech.intellijseniorflutter.listeners.SelectedFileListener
import com.intellij.openapi.components.Service
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.messages.Topic

@Service(Service.Level.PROJECT)
class SelectedFileService(private val project: Project) : FileEditorManagerListener {

    private val fileEditorConnection = project.messageBus.connect()

    var currentlySelectedFile: VirtualFile? = null

    companion object {
        val FILE_SELECTED_TOPIC = Topic.create("File opened", SelectedFileListener::class.java)
    }

    init {
        fileEditorConnection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, this)
    }

    override fun selectionChanged(event: FileEditorManagerEvent) {
        val file = event.newFile
        currentlySelectedFile = file
        project.messageBus.syncPublisher(FILE_SELECTED_TOPIC).onFileSelected(file)
    }
}