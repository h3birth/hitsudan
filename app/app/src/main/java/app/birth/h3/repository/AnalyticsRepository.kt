package app.birth.h3.repository

import com.google.firebase.analytics.FirebaseAnalytics

interface AnalyticsRepository {
    var firebaseAnalytics: FirebaseAnalytics
    fun initialize()
    fun sendClick(itemName: String)
    fun sendScreenView(screenName: String)

    enum class AnalyticsEvent {
        CLICK,
    }
}
