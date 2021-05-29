package app.birth.h3.repository

import android.content.Context
import android.provider.Settings.Global.getString
import app.birth.h3.R
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharePreferenceRepositoryImpl @Inject constructor(
        @ApplicationContext val context: Context
): SharePreferenceRepository {
    private val spf = context.getSharedPreferences(context.getString(R.string.pref_pen_set), Context.MODE_PRIVATE)

    override fun getPenWeight() = spf.getInt(context.getString(R.string.pref_key_pen_weight), 10)
    override fun setPenWeight(value: Int) {
        with (spf.edit()) {
            putInt(context.getString(R.string.pref_key_pen_weight), value)
            commit()
        }
    }

    override fun getPenColor() = spf.getInt(context.getString(R.string.pref_key_pen_color), 0)
    override fun setPenColor(value: Int) {
        with (spf.edit()) {
            putInt(context.getString(R.string.pref_key_pen_color), value)
            commit()
        }
    }
}
