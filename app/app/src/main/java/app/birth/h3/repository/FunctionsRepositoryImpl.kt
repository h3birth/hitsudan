package app.birth.h3.repository

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FunctionsRepositoryImpl @Inject constructor(
        @ApplicationContext val context: Context
): FunctionsRepository {
    override var functions: FirebaseFunctions? = null

    fun initilize() {
        functions = FirebaseFunctions.getInstance()
    }

    override fun annotateImage(requestJson: String): Task<JsonElement>? {
        return functions
                ?.getHttpsCallable("annotateImage")
                ?.call(requestJson)
                ?.continueWith { task ->
                    // This continuation runs on either success or failure, but if the task
                    // has failed then result will throw an Exception which will be
                    // propagated down.
                    val result = task.result?.data
                    JsonParser.parseString(Gson().toJson(result))
                }
    }
}
