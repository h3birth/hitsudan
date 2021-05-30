package app.birth.h3.util

import android.graphics.Color
import android.view.View
import android.widget.Button
import androidx.databinding.BindingAdapter

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
