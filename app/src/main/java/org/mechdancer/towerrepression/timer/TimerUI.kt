package org.mechdancer.towerrepression.timer

import android.support.v4.app.Fragment
import android.view.View
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.linearLayout
import org.jetbrains.anko.textView

class TimerUI : AnkoComponent<Fragment> {
    override fun createView(ui: AnkoContext<Fragment>): View = with(ui) {
        linearLayout {
            textView("www")
        }
    }

}