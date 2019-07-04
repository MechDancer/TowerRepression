package org.mechdancer.towerrepression.scorer

import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
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
import org.jetbrains.anko.support.v4.toast
import org.mechdancer.towerrepression.ClearEvent
import org.mechdancer.towerrepression.FieldEvent
import org.mechdancer.towerrepression.R

class ScorerFragment : Fragment(), View.OnClickListener {


    //Scorer state
    private val towers = mutableMapOf<Button, Tower>()
    private val zones = mutableMapOf<Button, Zone>()

    //Picker state
    private var current = CubeColor.None

    private val deleteMode
        get() = deleteSwitch.isChecked


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        EventBus.getDefault().register(this)
        return UI { ScorerUI().createView(this) }.view
    }

    @Suppress("DEPRECATION")
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
            it.backgroundColorResource = R.color.TRANSPARENT
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
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onClear(event: ClearEvent) {
        towers.values.forEach { it.content.indices.forEach { i -> it.content[i] = 0 } }
        zones.values.forEach { it.content.indices.forEach { i -> it.content[i] = 0 } }
    }

    override fun onClick(view: View) {
        if (view is Button) {
            if (current == CubeColor.None) return
            val dValue = if (deleteMode) -1 else 1
            Snackbar.make(constraintButtons, "场地更新 ${view.text}", Snackbar.LENGTH_SHORT).show()
            fun Int.check() = if (this < 0) {
                toast("此种颜色方块已经空了")
                0
            } else this
            towers[view]?.let { map ->
                map.content.getOrNull(current.index)
                    ?.let { map.content[map.content.indexOf(it)] = (it + dValue).check() }
            }
            zones[view]?.let { map ->
                map.content.getOrNull(current.index)
                    ?.let { map.content[map.content.indexOf(it)] = (it + dValue).check() }
            }
            Log.d(javaClass.name, towers.values.joinToString())
            Log.d(javaClass.name, zones.values.joinToString())
            postData()
        } else if (view is ImageButton)
            changeColor(view.toCubeColor().takeIf { it != current } ?: CubeColor.None)

    }

    private fun postData() {
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

    //Scorer Impl

    //Picker Impl

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