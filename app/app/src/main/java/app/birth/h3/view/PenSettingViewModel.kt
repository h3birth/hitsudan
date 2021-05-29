package app.birth.h3.view

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import app.birth.h3.model.Color
import app.birth.h3.repository.ColorRepository
import app.birth.h3.repository.SharePreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PenSettingViewModel @Inject constructor(
        val spf: SharePreferenceRepository,
        val colors: ColorRepository
): ViewModel() {
    val penWeight = MutableLiveData(spf.getPenWeight())
    var penColor = MutableLiveData(spf.getPenColor())

    val isBlack = Transformations.map (penColor) { isSelect(colors.black) }
    val isBlue = Transformations.map (penColor) { isSelect(colors.blue) }
    val isGreen = Transformations.map (penColor) { isSelect(colors.green) }


    private fun isSelect(color: Color): String {
        return if((penColor.value ?: 0 + 1) == color.id) "●" else ""
    }

    fun onComplete() {
        spf.setPenWeight(penWeight.value ?: 10)
        spf.setPenColor(penColor.value ?: 1)
    }

    fun onClickPenColor(color: Color) {
        penColor.postValue(color.id)
    }
}
