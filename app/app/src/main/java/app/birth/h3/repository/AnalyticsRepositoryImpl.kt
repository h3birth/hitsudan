package app.birth.h3.repository

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class AnalyticsRepositoryImpl @Inject constructor(): AnalyticsRepository {
    override lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun initialize() {
        firebaseAnalytics = Firebase.analytics
    }
    override fun sendClick(itemName: String) {
        firebaseAnalytics.logEvent(AnalyticsRepository.AnalyticsEvent.CLICK.name) {
            param(FirebaseAnalytics.Param.ITEM_NAME, itemName)
        }
    }
    override fun sendScreenView(screenName: String) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        }
    }
}
