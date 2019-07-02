package org.mechdancer.towerrepression.table

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.support.v4.UI

class TableFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val ui = UI { TableUI().createView(this) }.view
        return ui
    }
}