package com.austintech.intellijseniorflutter.services

import com.austintech.intellijseniorflutter.network.HttpClient
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.VirtualFile
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

@Service(Service.Level.PROJECT)
class EditCodeService(private val project: Project) {

    private val client = HttpClient.client


    fun sendEditTask(file: VirtualFile, userPrompt: String, manualImports: String) {
        val rootPath = ProjectFileIndex.getInstance(project).getContentRootForFile(file)?.path
        val contents = String(file.contentsToByteArray())
        val map = mapOf(
            "sourceCodeToBeEdited" to contents,
            "userPrompt" to userPrompt,
            "classDependencies" to manualImports,
            "projectRootPath" to rootPath
        )

        sendEditTaskRequest(map)
    }

    private fun sendEditTaskRequest(map: Map<String, String?>) {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = Json.encodeToString(map).toRequestBody(mediaType)

        val request = Request.Builder()
            .url("http://localhost:8080/edit")
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            val responseBody = response.body?.string()

            if (response.code == 200 && responseBody != null) {
                val successResponse = Json.decodeFromString(SuccessResponse.serializer(), responseBody)
                println("New Source Code: ${successResponse.newSourceCode}")
            }

            if (responseBody == null) {
                println("Error: ${response.message}")
                return
            }

            val errorResponse = Json.decodeFromString(ErrorResponse.serializer(), responseBody)
            println("Error: ${errorResponse.error}")
        }
    }
}

@Serializable
data class SuccessResponse(val newSourceCode: String)

@Serializable
data class ErrorResponse(val error: String)