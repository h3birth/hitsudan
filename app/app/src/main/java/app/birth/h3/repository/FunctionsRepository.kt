package app.birth.h3.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.gson.JsonElement

interface FunctionsRepository {
    var functions: FirebaseFunctions?

    fun annotateImage(requestJson: String): Task<JsonElement>?
}
