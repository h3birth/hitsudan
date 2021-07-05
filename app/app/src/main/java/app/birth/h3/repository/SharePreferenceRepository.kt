package app.birth.h3.repository

import android.content.Context
import dagger.Module
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface SharePreferenceRepository {
    fun getPenWeight(): Int
    fun setPenWeight(value: Int)
    fun getPenColor(): Int
    fun setPenColor(value: Int)
    fun getBackgroundColor(): Int
    fun setBackgroundColor(value: Int)
    fun getShownEraser(): Boolean
    fun setShownEraser(value: Boolean)
    fun getCustomPenColor(): String?
    fun setCustomPenColor(value: String)
    fun getUseCustomPen(): Boolean
    fun setUseCustomPen(value: Boolean)
}
