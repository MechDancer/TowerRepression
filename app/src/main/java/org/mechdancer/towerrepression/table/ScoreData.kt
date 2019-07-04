package org.mechdancer.towerrepression.table

import com.bin.david.form.annotation.SmartColumn
import com.bin.david.form.annotation.SmartTable


@SmartTable(name = "分数")
data class ScoreData(
    @SmartColumn(id = 1, name = "\t")
    val team: String,
    @SmartColumn(id = 2, name = "橙色")
    val orangeScore: Int,
    @SmartColumn(id = 3, name = "绿色")
    val greenScore: Int,
    @SmartColumn(id = 4, name = "紫色")
    val purpleScore: Int,
    @SmartColumn(id = 5, name = "全部")
    val totalScore: Int
)


@SmartTable(name = "奖励")
data class BonusData(
    @SmartColumn(id = 1, name = "\t")
    val team: String,
    @SmartColumn(id = 2, name = "橙色")
    val orangeBonus: Int,
    @SmartColumn(id = 3, name = "绿色")
    val greenBonus: Int,
    @SmartColumn(id = 4, name = "紫色")
    val purpleBonus: Int,
    @SmartColumn(id = 5, name = "全部")
    val totalBonus: Int
)

@SmartTable(name = "个数")
data class BlockData(
    @SmartColumn(id = 1, name = "\t")
    val team: String,
    @SmartColumn(id = 2, name = "橙色")
    val orangeCount: Int,
    @SmartColumn(id = 3, name = "绿色")
    val greenCount: Int,
    @SmartColumn(id = 4, name = "紫色")
    val purpleCount: Int,
    @SmartColumn(id = 5, name = "全部")
    val totalCount: Int
)



