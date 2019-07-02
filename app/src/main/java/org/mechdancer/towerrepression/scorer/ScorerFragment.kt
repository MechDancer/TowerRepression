package org.mechdancer.towerrepression.scorer

import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import kotlinx.android.synthetic.main.fragment_scorer.*
import org.jetbrains.anko.forEachChild
import org.jetbrains.anko.support.v4.UI

class ScorerFragment : Fragment(), View.OnClickListener {

    //Scores
    private val bonus = IntArray(3)
    private val totalScore = IntArray(3)

    //Scorer state
    private val towers = mutableMapOf<Button, Tower>()
    private val zones = mutableMapOf<Button, Zone>()

    //Picker state
    private var current = CubeColor.Null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        UI { ScorerUI().createView(this) }.view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        constraintButtons.forEachChild {
            it.setOnClickListener(this)
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

        zones[redGoalZone] = Zone()
        zones[redStartingWall] = Zone()
        zones[blueGoalZone] = Zone()
        zones[blueStartingWall] = Zone()
    }

    override fun onClick(view: View) {
        if (view is Button) {
            towers[view]?.let { it.content[current.index]++ }
            zones[view]?.let { it.content[current.index]++ }
        } else if (view is ImageButton)
            changeColor(view.toCubeColor().takeIf { it != current } ?: CubeColor.Null)

    }


    //Scorer Impl

    //Picker Impl

    private fun CubeColor.toPickerButton() = when (this) {
        CubeColor.Orange -> pickerOrange
        CubeColor.Green -> pickerGreen
        CubeColor.Purple -> pickerPurple
        CubeColor.Null -> null
    }

    private fun ImageButton.toCubeColor() = CubeColor.values().find { it.toPickerButton() == this }!!

    private infix fun View.scaleTo(s: Float) {
        ObjectAnimator.ofFloat(this, "scaleX", s).setDuration(300).start()
        ObjectAnimator.ofFloat(this, "scaleY", s).setDuration(300).start()
    }

    private fun changeColor(color: CubeColor?) {
        current = color ?: CubeColor.Null
        current.toPickerButton()?.let {
            it scaleTo 1.3f
        }
        current.complementary.mapNotNull { it.toPickerButton() }.forEach {
            it scaleTo 1.0f
        }
    }
}