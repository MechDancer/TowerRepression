package org.mechdancer.towerrepression.table

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
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
import org.mechdancer.towerrepression.TableRefreshRequest
import org.mechdancer.towerrepression.scorer.CubeColor

@Suppress("UNUSED_PARAMETER")
class TableFragment : Fragment() {

    private lateinit var bonusTable: SmartTable<BonusData>
    private lateinit var blockTable: SmartTable<BlockData>
    private lateinit var scoreTable: SmartTable<ScoreData>
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val team
        get() = PreferenceManager.getDefaultSharedPreferences(context).getString("team", "") ?: ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(javaClass.name, "创建布局")
        val ui = UI { TableUI().createView(this) }.view
        bonusTable = ui.find(TableUI.TABLE_BONUS_ID)
        blockTable = ui.find(TableUI.TABLE_BLOCK_ID)
        scoreTable = ui.find(TableUI.TABLE_SCORE_ID)
        swipeRefreshLayout = ui.find(TableUI.SWIPE_LAYOUT_ID)
        swipeRefreshLayout.setOnRefreshListener {
            Log.i(javaClass.name, "开始下拉刷新，向事件总线推送表格刷新请求")
            EventBus.getDefault().post(TableRefreshRequest)
            Snackbar.make(blockTable, "刷新成功", Snackbar.LENGTH_SHORT).show()
        }
        EventBus.getDefault().register(this)
        onClear(ClearEvent)
        return ui
    }


    override fun onDestroy() {
        Log.i(javaClass.name, "销毁")
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFieldUpdate(event: FieldEvent) {
        Log.i(javaClass.name, "收到场地数据，绘制表格")
        Log.d(javaClass.name, event.toString())
        with(event) {
            val redOrangeBonus = towerData[CubeColor.Orange.index]
            val redGreenBonus = towerData[CubeColor.Green.index]
            val redPurpleBonus = towerData[CubeColor.Purple.index]
            val redOrangeCount = zoneData[CubeColor.Orange.index]
            val redGreenCount = zoneData[CubeColor.Green.index]
            val redPurpleCount = zoneData[CubeColor.Purple.index]
            val redOrangeScore = redOrangeCount * (redOrangeBonus + 1)
            val redGreenScore = redGreenCount * (redGreenBonus + 1)
            val redPurpleScore = redPurpleCount * (redPurpleBonus + 1)
            val redTotalBonus = redOrangeBonus + redGreenBonus + redPurpleBonus
            val redTotalCount = zoneData.take(3).sum()
            val redTotalScore = redOrangeScore + redGreenScore + redPurpleScore

            val blueOrangeBonus = towerData[CubeColor.Orange.index]
            val blueGreenBonus = towerData[CubeColor.Green.index]
            val bluePurpleBonus = towerData[CubeColor.Purple.index]
            val blueOrangeCount = zoneData[CubeColor.Orange.index + 3]
            val blueGreenCount = zoneData[CubeColor.Green.index + 3]
            val bluePurpleCount = zoneData[CubeColor.Purple.index + 3]
            val blueOrangeScore = blueOrangeCount * (blueOrangeBonus + 1)
            val blueGreenScore = blueGreenCount * (blueGreenBonus + 1)
            val bluePurpleScore = bluePurpleCount * (bluePurpleBonus + 1)
            val blueTotalBonus = blueOrangeBonus + blueGreenBonus + bluePurpleBonus
            val blueTotalCount = zoneData.takeLast(3).sum()
            val blueTotalScore = blueOrangeScore + blueGreenScore + bluePurpleScore

            fun adaptTeam(int: Int) = (if (team == "蓝方") -1 else 1) * int

            val dOrangeBonus = adaptTeam(redOrangeBonus - blueOrangeBonus)
            val dGreenBonus = adaptTeam(redGreenBonus - blueGreenBonus)
            val dPurpleBonus = adaptTeam(redPurpleBonus - bluePurpleBonus)
            val dOrangeCount = adaptTeam(redOrangeCount - blueOrangeCount)
            val dGreenCount = adaptTeam(redGreenCount - blueGreenCount)
            val dPurpleCount = adaptTeam(redPurpleCount - bluePurpleCount)
            val dOrangeScore = adaptTeam(redOrangeScore - blueOrangeScore)
            val dGreenScore = adaptTeam(redGreenScore - blueGreenScore)
            val dPurpleScore = adaptTeam(redPurpleScore - bluePurpleScore)
            val dTotalBonus = adaptTeam(redTotalBonus - blueTotalBonus)
            val dTotalCount = adaptTeam(redTotalCount - blueTotalCount)
            val dTotalScore = adaptTeam(redTotalScore - blueTotalScore)

            val redTeamBonus =
                BonusData(
                    "红方",
                    redOrangeBonus,
                    redGreenBonus,
                    redPurpleBonus,
                    redTotalBonus
                )
            val redTeamBlock =
                BlockData(
                    "红方",
                    redOrangeCount,
                    redGreenCount,
                    redPurpleCount,
                    redTotalCount
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
            val blueTeamBlock = BlockData(
                "蓝方",
                blueOrangeCount,
                blueGreenCount,
                bluePurpleCount,
                blueTotalCount
            )
            val blueTeamScore = ScoreData(
                "蓝方",
                blueOrangeScore,
                blueGreenScore,
                bluePurpleScore,
                blueTotalScore
            )


            val dName = if (team == "蓝方") "蓝方-红方" else "红方-蓝方"

            val dTeamBonus = BonusData(
                dName,
                dOrangeBonus,
                dGreenBonus,
                dPurpleBonus,
                dTotalBonus
            )
            val dTeamBlock = BlockData(
                dName,
                dOrangeCount,
                dGreenCount,
                dPurpleCount,
                dTotalCount
            )
            val dTeamScore = ScoreData(
                dName,
                dOrangeScore,
                dGreenScore,
                dPurpleScore,
                dTotalScore
            )
            bonusTable.setData(mutableListOf(redTeamBonus, blueTeamBonus, dTeamBonus))
            blockTable.setData(mutableListOf(redTeamBlock, blueTeamBlock, dTeamBlock))
            scoreTable.setData(mutableListOf(redTeamScore, blueTeamScore, dTeamScore))
            swipeRefreshLayout.isRefreshing = false
        }
    }

    @Subscribe
    fun onClear(event: ClearEvent) {
        Log.i(javaClass.name, "收到清空事件")
        onFieldUpdate(FieldEvent.empty())
    }
}