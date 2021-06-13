package app.birth.h3.util

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.util.SizeF
import android.util.TypedValue
import android.view.WindowInsets
import android.view.WindowManager

class ScreenUtil(val context: Context) {
    companion object {
        private const val NAVIGATION_BAR_MODE_3BUTTON = 0
        private const val NAVIGATION_BAR_MODE_2BUTTON = 1
        private const val NAVIGATION_BAR_MODE_GESTURAL = 2
    }

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val display = windowManager.defaultDisplay

    fun getScreenSize(): Point {
        val size = Point()
        display?.getRealSize(size)
        return size
    }

    fun getContentWidthByDeviceMargin(marginStart: Float, marginEnd: Float): Float {
        val screenSize = getScreenSize()
        return screenSize.x - (marginStart + marginEnd)
    }

    fun setAspectHeight(size: SizeF, width: Float): Float {
        val magnification = width / size.width
        return size.height * magnification
    }

    fun getStatusBarHeight(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            windowManager.currentWindowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars()).top
        } else {
            val rect = Rect()
            (context as? Activity)?.let {
                it.window.decorView.getWindowVisibleDisplayFrame(rect)
                rect.top
            } ?: 0
        }
    }

    fun getActionBarHeight(): Int {
        val tv = TypedValue()
        return if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
        } else 0
    }

    private fun getNavBarInteractionMode(): Int =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.resources.getInteger(Resources.getSystem().getIdentifier("config_navBarInteractionMode", "integer", "android"))
            } else {
                NAVIGATION_BAR_MODE_3BUTTON
            }

    fun isGestureNavigation(): Boolean =
            getNavBarInteractionMode() == NAVIGATION_BAR_MODE_GESTURAL
}
