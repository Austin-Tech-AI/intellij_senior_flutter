package com.austintech.intellijseniorflutter.listeners

import com.intellij.openapi.vfs.VirtualFile

interface SelectedFileListener {
    fun onFileSelected(currentlySelectedFile: VirtualFile?)
}