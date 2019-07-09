package org.mechdancer.towerrepression.scorer

import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import kotlinx.android.synthetic.main.fragment_scorer.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.forEachChild
import org.jetbrains.anko.support.v4.UI
import org.jetbrains.anko.support.v4.runOnUiThread
import org.jetbrains.anko.support.v4.toast
import org.mechdancer.towerrepression.CubeColor
import org.mechdancer.towerrepression.R
import org.mechdancer.towerrepression.event.ClearEvent
import org.mechdancer.towerrepression.event.FieldEvent
import org.mechdancer.towerrepression.event.TableRefreshRequest

@Suppress("UNUSED_PARAMETER", "DEPRECATION")
class ScorerFragment : Fragment(), View.OnClickListener {


    //Scorer state
    private val towers = mutableMapOf<Button, Tower>()
    private val zones = mutableMapOf<Button, Zone>()

    //Picker state
    private var current = CubeColor.None

    private val deleteMode
        get() = deleteSwitch.isChecked


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(javaClass.name, "创建布局")
        EventBus.getDefault().register(this)
        return UI { ScorerUI().createView(this) }.view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //Resize field image
        constraintButtons.background =
            BitmapDrawable(
                resources,
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.field), 1024, 1024, false)
            )

        constraintButtons.forEachChild {
            it.setOnClickListener(this)
            it.backgroundColorResource = R.color.WHITE_A
        }

        pickerButtons.forEachChild {
            it.setOnClickListener(this)
        }

        towers[towerA] = Tower()
        towers[towerB] = Tower()
        towers[towerC] = Tower()
        towers[towerD] = Tower()
        towers[towerE] = Tower()
        towers[towerG] = Tower()
        towers[towerH] = Tower()

        zones[redGoalZone] = Zone(false)
        zones[redStartingWall] = Zone(false)
        zones[blueGoalZone] = Zone(true)
        zones[blueStartingWall] = Zone(true)
    }

    override fun onDestroy() {
        Log.i(javaClass.name, "销毁")
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onClear(event: ClearEvent) {
        Log.i(javaClass.name, "收到清空事件")
        towers.values.forEach { it.content.indices.forEach { i -> it.content[i] = 0 } }
        zones.values.forEach { it.content.indices.forEach { i -> it.content[i] = 0 } }
        runOnUiThread {
            constraintButtons.forEachChild {
                it as Button
                it.setBlockCount(0, 0, 0)
            }
        }
    }

    private fun Button.setBlockCount(orangeCount: Int, greenCount: Int, purpleCount: Int) {
        textSize = 10f
        text = SpannableStringBuilder().apply {
            append(
                "■",
                ForegroundColorSpan(resources.getColor(R.color.ORANGE)),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            append(orangeCount.toString())
            append(
                "■",
                ForegroundColorSpan(resources.getColor(R.color.GREEN)),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            append(greenCount.toString())
            append(
                "■",
                ForegroundColorSpan(resources.getColor(R.color.PURPLE)),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            append(purpleCount.toString())
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onPreferenceUpdate(event: TableRefreshRequest) {
        Log.i(javaClass.name, "收到表格刷新请求，准备推送场地数据")
        postData()
    }


    override fun onClick(view: View) {
        if (view is Button) {
            if (current == CubeColor.None) return
            val dValue = if (deleteMode) -1 else 1

            Snackbar.make(constraintButtons, "场地更新", Snackbar.LENGTH_SHORT).show()

            fun Int.check() = if (this < 0) {
                toast("此种颜色方块已经空了")
                0
            } else this

            val content = towers[view]?.content ?: zones[view]?.content ?: throw RuntimeException()

            content[current.index] = (content[current.index] + dValue).check()


            val orangeCount = content[CubeColor.Orange.index]
            val greenCount = content[CubeColor.Green.index]
            val purpleCount = content[CubeColor.Purple.index]

            view.setBlockCount(orangeCount, greenCount, purpleCount)


            Log.d(javaClass.name, towers.values.joinToString())
            Log.d(javaClass.name, zones.values.joinToString())
            postData()
        } else if (view is ImageButton)
            changeColor(view.toCubeColor().takeIf { it != current } ?: CubeColor.None)

    }

    private fun postData() {
        Log.i(javaClass.name, "向事件总线推送场地数据")
        val orangeBonus = towers.values.sumBy { it.content[CubeColor.Orange.index] }
        val greenBonus = towers.values.sumBy { it.content[CubeColor.Green.index] }
        val purpleBonus = towers.values.sumBy { it.content[CubeColor.Purple.index] }
        val redOrangeCube = zones.values.filter { !it.isBlueTeam }.sumBy { it.content[CubeColor.Orange.index] }
        val redGreenCube = zones.values.filter { !it.isBlueTeam }.sumBy { it.content[CubeColor.Green.index] }
        val redPurpleCube = zones.values.filter { !it.isBlueTeam }.sumBy { it.content[CubeColor.Purple.index] }
        val blueOrangeCube = zones.values.filter { it.isBlueTeam }.sumBy { it.content[CubeColor.Orange.index] }
        val blueGreenCube = zones.values.filter { it.isBlueTeam }.sumBy { it.content[CubeColor.Green.index] }
        val bluePurpleCube = zones.values.filter { it.isBlueTeam }.sumBy { it.content[CubeColor.Purple.index] }
        EventBus.getDefault().post(
            FieldEvent(
                intArrayOf(orangeBonus, greenBonus, purpleBonus),
                intArrayOf(redOrangeCube, redGreenCube, redPurpleCube, blueOrangeCube, blueGreenCube, bluePurpleCube)
            )
        )
    }


    private fun CubeColor.toPickerButton() = when (this) {
        CubeColor.Orange -> pickerOrange
        CubeColor.Green -> pickerGreen
        CubeColor.Purple -> pickerPurple
        CubeColor.None -> null
    }

    private fun ImageButton.toCubeColor() = CubeColor.values().find { it.toPickerButton() == this }!!

    private infix fun View.scaleTo(s: Float) {
        ObjectAnimator.ofFloat(this, "scaleX", s).setDuration(80).start()
        ObjectAnimator.ofFloat(this, "scaleY", s).setDuration(80).start()
    }

    private fun changeColor(color: CubeColor?) {
        Log.i(javaClass.name, "更改颜色: $current -> $color")
        current = color ?: CubeColor.None
        Snackbar.make(constraintButtons, "选择了${getString(current.str())}", Snackbar.LENGTH_SHORT).show()
        current.toPickerButton()?.let {
            it scaleTo 1.3f
        }
        current.complementary.mapNotNull { it.toPickerButton() }.forEach {
            it scaleTo 1.0f
        }
    }
}