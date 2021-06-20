package app.birth.h3.repository

import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
        @ApplicationContext val context: Context
): AuthRepository {
    override var auth: FirebaseAuth? = null

    override val currentUser: FirebaseUser?
        get() = auth?.currentUser

    override val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())

    override var user: FirebaseUser? = null

    override fun new() {
        auth = FirebaseAuth.getInstance()
    }

    override fun intentFirebaseUI() = AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build()

    override fun saveUser(user: FirebaseUser) {
        this.user = user
    }
}
