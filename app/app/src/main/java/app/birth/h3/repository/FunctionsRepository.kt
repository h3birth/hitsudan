package app.birth.h3.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.gson.JsonElement
import com.google.gson.JsonObject

interface FunctionsRepository {
    var functions: FirebaseFunctions?

    fun initilize()

    fun annotateImage(requestJson: String): Task<JsonElement>?

    fun buildAnnotateImageRequest(base64encoded: String): JsonObject
}
