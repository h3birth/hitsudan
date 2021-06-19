package app.birth.h3

import android.app.Application
import app.birth.h3.util.MyTimberDebug
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(MyTimberDebug())
    }
}
