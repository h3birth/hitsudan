package app.birth.h3.repository

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

class FunctionsRepositoryImpl @Inject constructor(
        @ApplicationContext val context: Context
): FunctionsRepository {
    override var functions: FirebaseFunctions? = null

    override fun initilize() {
        functions = FirebaseFunctions.getInstance("asia-northeast1")
    }

    override fun annotateImage(requestJson: String): Task<JsonElement>? {
        return functions
                ?.getHttpsCallable("annotateImage")
                ?.call(requestJson)
                ?.continueWith { task ->
                    // This continuation runs on either success or failure, but if the task
                    // has failed then result will throw an Exception which will be
                    // propagated down.
                    task.exception?.let {
                        if(it is FirebaseFunctionsException) {
                            Timber.d("annotateImage exception code=${it.code}, message=${it.message.toString()}")
                        }
                    }

                    if(!task.isSuccessful) return@continueWith null

                    val result = task.result?.data
                    JsonParser.parseString(Gson().toJson(result))
                }
    }

    override fun buildAnnotateImageRequest(base64encoded: String): JsonObject {
        // Create json request to cloud vision
        val request = JsonObject()
        val image = JsonObject()
        image.add("content", JsonPrimitive(base64encoded))
        request.add("image", image)
        val feature = JsonObject()
        feature.add("type", JsonPrimitive("DOCUMENT_TEXT_DETECTION"))
        val features = JsonArray()
        features.add(feature)

        val imageContext = JsonObject()
        request.add("imageContext", imageContext)
        request.add("features", features)
        return request
    }
}
