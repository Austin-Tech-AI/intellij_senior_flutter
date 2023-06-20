package com.austintech.intellijseniorflutter.listeners

import com.austintech.intellijseniorflutter.services.EditCodeState

interface EditCodeListener {
    fun onStateChanged(editCodeState: EditCodeState)
}