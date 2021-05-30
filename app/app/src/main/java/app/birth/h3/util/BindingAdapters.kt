package app.birth.h3.util

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

@BindingAdapter("android:background")
fun Button.setBackground(hexCode: String) {
    val color = Color.parseColor(hexCode)
    this.setBackgroundColor(color)
}

@BindingAdapter("android:background")
fun View.setBackground(hexCode: String) {
    val color = Color.parseColor(hexCode)
    this.setBackgroundColor(color)
}

@BindingAdapter("backgroundTint")
fun FloatingActionButton.setBackgroundTint(hexCode: String) {
    val color = Color.parseColor(hexCode)
    this.backgroundTintList = ColorStateList.valueOf(color)
}

@BindingAdapter("android:textColor")
fun TextView.setTextColor(hexCode: String) {
    val color = Color.parseColor(hexCode)
    this.setTextColor(color)
}
