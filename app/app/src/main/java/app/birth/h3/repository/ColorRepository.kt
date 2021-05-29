package app.birth.h3.repository

import app.birth.h3.model.Color

interface ColorRepository {
    fun listPenColors() : List<Color>
    fun listBackgroundColors() : List<Color>
    fun getColorById(id: Int) : Color?

    val black: Color
    val blue: Color
    val green: Color
    val orange: Color
    val red: Color
    val brawn: Color
    val cyan: Color
    val teal: Color
    val yellow: Color
    val magenta: Color
    val grey: Color
    val indigo: Color
    val lime: Color
    val deepOrange: Color
    val purple: Color
    val white: Color
    val dark: Color
    val pink: Color
    val lightYellow: Color
    val lightBlue: Color
}
