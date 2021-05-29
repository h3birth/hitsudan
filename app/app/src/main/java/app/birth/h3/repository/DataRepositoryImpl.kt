package app.birth.h3.repository

import app.birth.h3.model.Color

class DataRepositoryImpl(): DataRepository {
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

    fun listPenColors() : List<Color> = colors.subList(0, 14)

    fun listBackgroundColors() : List<Color> = colors.subList(15, 19)
}
