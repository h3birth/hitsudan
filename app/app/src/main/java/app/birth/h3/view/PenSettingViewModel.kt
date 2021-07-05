package app.birth.h3.view

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import app.birth.h3.dispatcher.LoadImageDispatcher
import app.birth.h3.model.Color
import app.birth.h3.repository.ColorRepository
import app.birth.h3.repository.SharePreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PenSettingViewModel @Inject constructor(
        val spf: SharePreferenceRepository,
        val colors: ColorRepository,
        val loadImageDispatcher: LoadImageDispatcher
): ViewModel() {
    val penWeight = MutableLiveData(spf.getPenWeight())
    var penColor = MutableLiveData(spf.getPenColor())
    var backgroundColor = MutableLiveData(spf.getBackgroundColor())
    val shownEraser = MutableLiveData(spf.getShownEraser())
    var useCustomPenHex = MutableLiveData(spf.getCustomPenColor())

    // color
    val isBlack = Transformations.map (penColor) { isSelect(colors.black) }
    val isBlue = Transformations.map (penColor) { isSelect(colors.blue) }
    val isGreen = Transformations.map (penColor) { isSelect(colors.green) }
    val isOrange = Transformations.map (penColor) { isSelect(colors.orange) }
    val isRed = Transformations.map (penColor) { isSelect(colors.red) }
    val isBrawn = Transformations.map (penColor) { isSelect(colors.brawn) }
    val isCyan = Transformations.map (penColor) { isSelect(colors.cyan) }
    val isTeal = Transformations.map (penColor) { isSelect(colors.teal) }
    val isYellow = Transformations.map (penColor) { isSelect(colors.yellow) }
    val isMagenta = Transformations.map (penColor) { isSelect(colors.magenta) }
    val isGrey = Transformations.map (penColor) { isSelect(colors.grey) }
    val isIndigo = Transformations.map (penColor) { isSelect(colors.indigo) }
    val isLime = Transformations.map (penColor) { isSelect(colors.lime) }
    val isDeepOrange = Transformations.map (penColor) { isSelect(colors.deepOrange) }
    val isPurple = Transformations.map (penColor) { isSelect(colors.purple) }

    // background
    val isWhite = Transformations.map (backgroundColor) { isBackgroundSelect(colors.white) }
    val isDark = Transformations.map (backgroundColor) { isBackgroundSelect(colors.dark) }
    val isPink = Transformations.map (backgroundColor) { isBackgroundSelect(colors.pink) }
    val isLightYellow = Transformations.map (backgroundColor) { isBackgroundSelect(colors.lightYellow) }
    val isLightBlue = Transformations.map (backgroundColor) { isBackgroundSelect(colors.lightBlue) }

    val isLoadImageBitmap = loadImageDispatcher.loadImage.asLiveData().map {
        it != null
    }
    val noBackgroundColorVisibility = isLoadImageBitmap.map {
        if(it) View.VISIBLE else View.GONE
    }

    val customPenPicker = MutableLiveData(false)
    val customPenPickerVisibility = customPenPicker.map {
        if(it) View.VISIBLE else View.GONE
    }
    val presetPenColorVisibility = customPenPicker.map {
        if(it) View.INVISIBLE else View.VISIBLE
    }

    private fun isSelect(color: Color): String {
        return if((penColor.value ?: 0 + 1) == color.id) "●" else ""
    }
    private fun isBackgroundSelect(color: Color): String {
        return if((backgroundColor.value ?: 0 + 1) == color.id) "●" else ""
    }

    fun onComplete() {
        spf.setPenWeight(penWeight.value ?: 10)
        spf.setPenColor(penColor.value ?: 1)
        spf.setBackgroundColor(backgroundColor.value ?: 1)
        spf.setShownEraser(shownEraser.value ?: false)
        if(customPenPicker.value == true) {
            spf.setUseCustomPen(true)
            val hexCode = "#${useCustomPenHex.value ?: "000000"}"
            spf.setCustomPenColor(hexCode)
        }
    }

    fun onClickPenColor(color: Color) {
        penColor.postValue(color.id)
    }

    fun onClickBackgroundColor(color: Color) {
        if(isLoadImageBitmap.value != true) backgroundColor.postValue(color.id)
    }

    fun onClickCustomPenColor() {
        customPenPicker.postValue(customPenPicker.value?.not())
    }

    fun setPenColorOnPicker(colorInt: Int) {
        val hex = Integer.toHexString(colorInt)
        useCustomPenHex.postValue(hex)
    }
}
