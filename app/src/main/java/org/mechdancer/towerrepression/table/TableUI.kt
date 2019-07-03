package org.mechdancer.towerrepression.table

import android.support.v4.app.Fragment
import android.view.View
import com.bin.david.form.core.SmartTable
import com.bin.david.form.data.column.Column
import com.bin.david.form.data.table.TableData
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.custom.customView

class TableUI : AnkoComponent<Fragment> {

    override fun createView(ui: AnkoContext<Fragment>): View = with(ui) {
        linearLayout {
            customView<SmartTable<Unit>> {
            }.lparams(matchParent, matchParent)
        }
    }
}