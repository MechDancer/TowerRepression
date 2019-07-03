package org.mechdancer.towerrepression.table

import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.bin.david.form.core.SmartTable
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.customView

class TableUI : AnkoComponent<Fragment> {

    companion object {
        const val TABLE_BONUS_ID = 8331
        const val TABLE_SCORE_ID = 2333
    }

    override fun createView(ui: AnkoContext<Fragment>): View = with(ui) {
        linearLayout {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            leftPadding=dip(30)
            lparams(matchParent, matchParent)
            customView<SmartTable<BonusData>> {
                id = TABLE_BONUS_ID
                config.isShowYSequence = false
                config.isShowXSequence = false
            }.lparams(matchParent, dip(150)) {
                gravity = Gravity.CENTER_HORIZONTAL
                topMargin = 350
            }
            customView<SmartTable<ScoreData>> {
                id = TABLE_SCORE_ID
                config.isShowYSequence = false
                config.isShowXSequence = false
            }.lparams(matchParent, dip(150)) {
                gravity = Gravity.CENTER_HORIZONTAL
                topMargin = 100
            }
        }
    }
}