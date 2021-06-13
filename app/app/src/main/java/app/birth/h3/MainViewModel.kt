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
import app.birth.h3.repository.AnalyticsRepository
import app.birth.h3.repository.ColorRepository
import app.birth.h3.repository.SharePreferenceRepository
import app.birth.h3.util.BottomToolbarMode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
        val spf: SharePreferenceRepository,
        val colors: ColorRepository,
        val analytics: AnalyticsRepository
) : ViewModel(), LifecycleObserver {
    val backgroundColor = MutableLiveData(getColor(spf.getBackgroundColor()))
    val shownEraser = MutableLiveData(spf.getShownEraser())
    val eraserButtonVisibility = Transformations.map(shownEraser) {
        if(it) View.VISIBLE else View.GONE
    }
    val onEraser = MutableLiveData(false)
    val eraserButtonColor = Transformations.map(onEraser) {
        if(it) "#009688" else "#B5B5B5"
    }
    val eraserText = Transformations.map(onEraser) {
        if(it) "消しゴムON" else "消しゴムOFF"
    }
    val eraserTextColor = Transformations.map(backgroundColor) {
        if(it == colors.dark.code) "#FFFFFF" else "#333333"
    }

    val bottomToolbarMode = MutableLiveData(BottomToolbarMode.Close)
    val toolbarTabRotation = Transformations.map(bottomToolbarMode) { when(it){
        BottomToolbarMode.Open -> 180
        else -> 0
    } }
    val toolbarBottomVisibility =  Transformations.map(bottomToolbarMode) { when(it){
        BottomToolbarMode.Open -> View.VISIBLE
        else -> View.GONE
    } }

    fun getColor(id: Int): String {
        val color = colors.getColorById(id) ?: colors.white
        return color.code
    }

    fun onSettingBackground() {
        backgroundColor.postValue(getColor(spf.getBackgroundColor()))
        shownEraser.postValue(spf.getShownEraser())
    }

    fun onClickEraser() {
        onEraser.postValue(onEraser.value?.not())
    }

    fun onClickBottomToolbar() {
        when(bottomToolbarMode.value) {
            BottomToolbarMode.Open -> bottomToolbarMode.postValue(BottomToolbarMode.Close)
            BottomToolbarMode.Close -> bottomToolbarMode.postValue(BottomToolbarMode.Open)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        Log.i(this.javaClass.simpleName, "onCreate")
        analytics.initialize()
    }
}
