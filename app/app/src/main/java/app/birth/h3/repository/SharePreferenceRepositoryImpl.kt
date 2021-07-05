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

    override fun getBackgroundColor() = spf.getInt(context.getString(R.string.pref_key_background_color), 0)
    override fun setBackgroundColor(value: Int) {
        with (spf.edit()) {
            putInt(context.getString(R.string.pref_key_background_color), value)
            commit()
        }
    }

    override fun getShownEraser(): Boolean = spf.getBoolean(context.getString(R.string.pref_key_shown_eraser), false)
    override fun setShownEraser(value: Boolean) {
        with (spf.edit()) {
            putBoolean(context.getString(R.string.pref_key_shown_eraser), value)
            commit()
        }
    }

    override fun getCustomPenColor(): String? = spf.getString(context.getString(R.string.pref_custom_pen_color), "#000000")
    override fun setCustomPenColor(value: String) {
        with (spf.edit()) {
            putString(context.getString(R.string.pref_custom_pen_color), value)
            commit()
        }
    }

    override fun getUseCustomPen(): Boolean = spf.getBoolean(context.getString(R.string.pref_use_custom_pen), false)
    override fun setUseCustomPen(value: Boolean) {
        with (spf.edit()) {
            putBoolean(context.getString(R.string.pref_use_custom_pen), value)
            commit()
        }
    }
}
