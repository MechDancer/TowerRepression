package org.mechdancer.towerrepression.scorer

import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import org.mechdancer.towerrepression.R

enum class CubeColor(@ColorRes val resFile: Int, val index: Int) {
    Orange(R.color.ORANGE, 0),
    Green(R.color.GREEN, 1),
    Purple(R.color.PURPLE, 2),
    None(R.color.TRANSPARENT, 3);

    val complementary by lazy { values().filter { it != this } }

    @StringRes
    fun str(): Int = when (this) {
        Orange -> R.string.cube_color_orange
        Green -> R.string.cube_color_green
        Purple -> R.string.cube_color_purple
        None -> R.string.cube_color_none
    }
}