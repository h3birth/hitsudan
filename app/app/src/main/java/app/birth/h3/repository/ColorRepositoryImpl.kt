package app.birth.h3.repository

import app.birth.h3.model.Color
import javax.inject.Inject

class ColorRepositoryImpl @Inject constructor(): ColorRepository {
    private val colors = listOf(
            Color(1, "Black", "#212121"),
            Color(2, "Blue", "#2196f3"),
            Color(3, "Green", "#4caf50"),
            Color(4, "Orange", "#FF9800"),
            Color(5, "Red", "#f44336"),
            Color(6, "Brawn", "#795548"),
            Color(7, "Cyan", "#00BCD4"),
            Color(8, "Teal", "#009688"),
            Color(9, "Yellow", "#FFEB3B"),
            Color(10, "Magenta", "#E91E63"),
            Color(11, "Grey", "#9E9E9E"),
            Color(12, "Indigo", "#3F51B5"),
            Color(13, "Lime", "#CDDC39"),
            Color(14, "DeepOrange", "#FF5722"),
            Color(15, "People", "#9C27B0"),
            Color(16, "White", "#FFFFFF"),
            Color(17, "Dark", "#404040"),
            Color(18, "Pink", "#FFCDD2"),
            Color(19, "LightYellow", "#FFF9C4"),
            Color(20, "LightBlue", "#BBDEFB")
    )
    override val customColorId: Int = 99
    override var customPenColor: Color? = null

    override fun listPenColors() : List<Color> = colors.subList(0, 14)

    override fun listBackgroundColors() : List<Color> = colors.subList(15, 19)

    override fun getColorById(id: Int) = when(id == 99 && customPenColor != null) {
        true -> customPenColor
        else -> colors.find {
            it.id == id
        }
    }

    override fun setCutomPenColor(colorHex: String) {
        customPenColor = Color(customColorId, "Custom", "#${colorHex}")
    }

    override val black = colors.get(0)
    override val blue = colors.get(1)
    override val green = colors.get(2)
    override val orange = colors.get(3)
    override val red = colors.get(4)
    override val brawn = colors.get(5)
    override val cyan = colors.get(6)
    override val teal = colors.get(7)
    override val yellow = colors.get(8)
    override val magenta = colors.get(9)
    override val grey = colors.get(10)
    override val indigo = colors.get(11)
    override val lime = colors.get(12)
    override val deepOrange = colors.get(13)
    override val purple = colors.get(14)
    override val white = colors.get(15)
    override val dark = colors.get(16)
    override val pink = colors.get(17)
    override val lightYellow = colors.get(18)
    override val lightBlue = colors.get(19)
}
