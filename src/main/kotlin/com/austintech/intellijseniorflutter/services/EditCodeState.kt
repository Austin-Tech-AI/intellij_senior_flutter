package com.austintech.intellijseniorflutter.services

sealed class EditCodeState {
    object AwaitingUserInput : EditCodeState()
    object Loading : EditCodeState()
    data class Loaded(val newCode: String) : EditCodeState()
}
