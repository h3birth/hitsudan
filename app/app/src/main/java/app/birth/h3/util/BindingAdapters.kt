package app.birth.h3.util

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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

@BindingAdapter("bitmap", "scaleType")
fun setSrcDrawable(imageView: ImageView, bitmap: Bitmap?, scaleType: String) {
    if (bitmap == null) return

    val options = when (scaleType) {
        "centerCrop" -> RequestOptions().centerCrop()
        "centerInside" -> RequestOptions().centerInside()
        else -> RequestOptions()
    }
    Glide.with(imageView.context).load(bitmap).apply(options).into(imageView)
}

@BindingAdapter("android:tint")
fun ImageView.setTint(hexCode: String) {
    val color = Color.parseColor(hexCode)
    this.imageTintList = ColorStateList.valueOf(color)
}
