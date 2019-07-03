package org.mechdancer.towerrepression.table

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bin.david.form.core.SmartTable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.UI
import org.mechdancer.towerrepression.ClearEvent
import org.mechdancer.towerrepression.FieldEvent
import org.mechdancer.towerrepression.scorer.CubeColor

class TableFragment : Fragment() {

    private lateinit var bonusTable: SmartTable<BonusData>
    private lateinit var scoreTable: SmartTable<ScoreData>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val ui = UI { TableUI().createView(this) }.view
        bonusTable = ui.find(TableUI.TABLE_BONUS_ID)
        scoreTable = ui.find(TableUI.TABLE_SCORE_ID)
        EventBus.getDefault().register(this)
        onClear(ClearEvent)
        return ui
    }


    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFieldUpdate(event: FieldEvent) {
        Log.d(javaClass.name, event.toString())
        with(event) {
            val redOrangeBonus = towerData[CubeColor.Orange.index]
            val redGreenBonus = towerData[CubeColor.Green.index]
            val redPurpleBonus = towerData[CubeColor.Purple.index]
            val redOrangeScore = zoneData[CubeColor.Orange.index] * (redOrangeBonus + 1)
            val redGreenScore = zoneData[CubeColor.Green.index] * (redGreenBonus + 1)
            val redPurpleScore = zoneData[CubeColor.Purple.index] * (redPurpleBonus + 1)
            val redTotalBonus = redOrangeBonus + redGreenBonus + redPurpleBonus
            val redTotalScore = redOrangeScore + redGreenScore + redPurpleScore

            val blueOrangeBonus = towerData[CubeColor.Orange.index]
            val blueGreenBonus = towerData[CubeColor.Green.index]
            val bluePurpleBonus = towerData[CubeColor.Purple.index]
            val blueOrangeScore = zoneData[CubeColor.Orange.index + 3] * (blueOrangeBonus + 1)
            val blueGreenScore = zoneData[CubeColor.Green.index + 3] * (blueGreenBonus + 1)
            val bluePurpleScore = zoneData[CubeColor.Purple.index + 3] * (bluePurpleBonus + 1)
            val blueTotalBonus = blueOrangeBonus + blueGreenBonus + bluePurpleBonus
            val blueTotalScore = blueOrangeScore + blueGreenScore + bluePurpleScore

            val dOrangeBonus = redOrangeBonus - blueOrangeBonus
            val dGreenBonus = redGreenBonus - blueGreenBonus
            val dPurpleBonus = redPurpleBonus - bluePurpleBonus
            val dOrangeScore = redOrangeScore - blueOrangeScore
            val dGreenScore = redGreenScore - blueGreenScore
            val dPurpleScore = redPurpleScore - bluePurpleScore
            val dTotalBonus = redTotalBonus - blueTotalBonus
            val dTotalScore = redTotalScore - blueTotalScore

            val redTeamBonus =
                BonusData(
                    "红方",
                    redOrangeBonus,
                    redGreenBonus,
                    redPurpleBonus,
                    redTotalBonus
                )
            val redTeamScore =
                ScoreData(
                    "红方",
                    redOrangeScore,
                    redGreenScore,
                    redPurpleScore,
                    redTotalScore
                )


            val blueTeamBonus =
                BonusData(
                    "蓝方",
                    blueOrangeBonus,
                    blueGreenBonus,
                    bluePurpleBonus,
                    blueTotalBonus
                )
            val blueTeamScore =
                ScoreData(
                    "蓝方",
                    blueOrangeScore,
                    blueGreenScore,
                    bluePurpleScore,
                    blueTotalScore
                )


            val dTeamBonus = BonusData(
                "差值",
                dOrangeBonus,
                dGreenBonus,
                dPurpleBonus,
                dTotalBonus
            )
            val dTeamScore = ScoreData(
                "差值",
                dOrangeScore,
                dGreenScore,
                dPurpleScore,
                dTotalScore
            )
            bonusTable.setData(mutableListOf(redTeamBonus, blueTeamBonus, dTeamBonus))
            scoreTable.setData(mutableListOf(redTeamScore, blueTeamScore, dTeamScore))

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onClear(event: ClearEvent) {
        onFieldUpdate(FieldEvent.empty())
    }
}