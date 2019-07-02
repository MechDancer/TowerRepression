package org.mechdancer.towerrepression.scorer

import android.support.annotation.ColorRes
import org.mechdancer.towerrepression.R

enum class CubeColor(@ColorRes val resFile: Int) {
    Orange(R.color.ORANGE),
    Green(R.color.GREEN),
    Purple(R.color.PURPLE),
    Null(R.color.TRANSPARENT);

    val complementary = values().filter { it != this }
    val index = values().indexOf(this)
}