package org.mechdancer.towerrepression

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class PagerAdapter(manager: FragmentManager, private val fragments: List<Fragment>) : FragmentPagerAdapter(manager) {

    override fun getItem(index: Int): Fragment = fragments[index]

    override fun getCount(): Int = fragments.size

}