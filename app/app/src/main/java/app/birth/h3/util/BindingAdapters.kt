package app.birth.h3.util

import android.graphics.Color
import android.widget.Button
import androidx.databinding.BindingAdapter

@BindingAdapter("android:background")
fun Button.setBackground(hexCode: String) {
    val color = Color.parseColor(hexCode)
    this.setBackgroundColor(color)
}
