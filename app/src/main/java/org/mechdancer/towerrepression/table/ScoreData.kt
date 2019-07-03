package org.mechdancer.towerrepression.table

import com.bin.david.form.annotation.SmartColumn
import com.bin.david.form.annotation.SmartTable


@SmartTable(name = "分数")
data class ScoreData(
    @SmartColumn(id = 1, name = "\t")
    val team: String,
    @SmartColumn(id = 2, name = "橙色分数")
    val orangeScore: Int,
    @SmartColumn(id = 3, name = "绿色分数")
    val greenScore: Int,
    @SmartColumn(id = 4, name = "紫色分数")
    val purple: Int,
    @SmartColumn(id = 5, name = "全部分数")
    val totalScore: Int
)


@SmartTable(name = "奖励")
data class BonusData(
    @SmartColumn(id = 1, name = "\t")
    val team: String,
    @SmartColumn(id = 2, name = "橙色奖励")
    val orangeBonus: Int,
    @SmartColumn(id = 3, name = "绿色奖励")
    val greenScore: Int,
    @SmartColumn(id = 4, name = "紫色奖励")
    val purple: Int,
    @SmartColumn(id = 5, name = "全部奖励")
    val totalScore: Int
)



