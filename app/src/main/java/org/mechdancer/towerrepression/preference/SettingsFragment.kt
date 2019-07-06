package org.mechdancer.towerrepression.preference

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import android.util.Log
import org.mechdancer.towerrepression.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_general)
        Log.i(javaClass.name, "创建布局")
    }
}