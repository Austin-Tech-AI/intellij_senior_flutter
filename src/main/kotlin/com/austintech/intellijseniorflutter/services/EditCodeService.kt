package com.austintech.intellijseniorflutter.services

import com.austintech.intellijseniorflutter.network.HttpClient
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.VirtualFile
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
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

        val moshiForMap = Moshi.Builder().build()
        val adapter = moshiForMap.adapter(Map::class.java)

        val jsonString = adapter.toJson(map)
        val body = jsonString.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("http://localhost:8080/edit")
            .post(body)
            .build()

        client.newCall(request).execute().use { response ->
            val responseBody = response.body?.string()

            if (responseBody == null) {
                println("Error: ${response.message}")
                return
            }

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            if (response.code == 200) {
                val jsonAdapter = moshi.adapter(SuccessResponse::class.java)
                val successResponse = jsonAdapter.fromJson(responseBody)
                println("New Source Code: ${successResponse?.newSourceCode}")
                return@use
            }

            val jsonAdapter = moshi.adapter(ErrorResponse::class.java)
            val errorResponse = jsonAdapter.fromJson(responseBody)
            println("Error: ${errorResponse?.error}")
        }
    }
}

@JsonClass(generateAdapter = true)
data class SuccessResponse(val newSourceCode: String)

@JsonClass(generateAdapter = true)
data class ErrorResponse(val error: String)