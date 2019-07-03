package org.mechdancer.towerrepression.scorer

import android.support.annotation.ColorRes
import org.mechdancer.towerrepression.R

enum class CubeColor(@ColorRes val resFile: Int) {
    Orange(R.color.ORANGE),
    Green(R.color.GREEN),
    Purple(R.color.PURPLE),
    None(R.color.TRANSPARENT);

    val complementary by lazy { values().filter { it != this } }
    val index by lazy { values().indexOf(this) }
}