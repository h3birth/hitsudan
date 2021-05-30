package app.birth.h3

import android.util.Log
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import app.birth.h3.model.Color
import app.birth.h3.repository.ColorRepository
import app.birth.h3.repository.SharePreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
        val spf: SharePreferenceRepository,
        val colors: ColorRepository
) : ViewModel() {
    val backgroundColor = MutableLiveData(getColor(spf.getBackgroundColor()))
    private val shownEraser = MutableLiveData(spf.getShownEraser())
    val eraserButtonVisibility = Transformations.map(shownEraser) {
        if(it) View.VISIBLE else View.GONE
    }

    fun getColor(id: Int): String {
        val color = colors.getColorById(id) ?: colors.white
        return color.code
    }

    fun onSettingBackground() {
        backgroundColor.postValue(getColor(spf.getBackgroundColor()))
        shownEraser.postValue(spf.getShownEraser())
    }
}
