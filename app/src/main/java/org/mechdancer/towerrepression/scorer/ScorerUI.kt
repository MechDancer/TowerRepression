package org.mechdancer.towerrepression.scorer

import android.support.v4.app.Fragment
import android.view.View
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.include
import org.mechdancer.towerrepression.R

class ScorerUI : AnkoComponent<Fragment> {
    override fun createView(ui: AnkoContext<Fragment>): View = with(ui) {
        include(R.layout.fragment_scorer)
    }

}