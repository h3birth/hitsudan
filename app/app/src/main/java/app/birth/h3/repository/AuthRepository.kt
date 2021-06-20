package app.birth.h3.repository

import android.content.Intent
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    var auth: FirebaseAuth?
    val currentUser: FirebaseUser?

    val providers : ArrayList<AuthUI.IdpConfig>

    var user: FirebaseUser?

    fun new()

    fun intentFirebaseUI(): Intent

    fun saveUser(user: FirebaseUser)
}
