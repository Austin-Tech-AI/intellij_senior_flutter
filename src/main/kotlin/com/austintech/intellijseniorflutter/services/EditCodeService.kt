package com.austintech.intellijseniorflutter.services

import com.intellij.openapi.components.Service
import com.intellij.util.io.HttpRequests
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Service(Service.Level.PROJECT)
class EditCodeService {

    fun sendEditTask(sourceCode: String, userPrompt: String, manualImports: String) {
        val map = mapOf(
            "sourceCodeToBeEdited" to sourceCode,
            "userPrompt" to userPrompt,
            "classDependencies" to manualImports
        )
        println(map)

        HttpRequests.post("http://localhost:8080/edit", "application/json")
            .connect {
                it.write(Json.encodeToString(map))
                it.readString()
            }
    }
}