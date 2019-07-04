package org.mechdancer.towerrepression.preference

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import org.mechdancer.towerrepression.R

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = "settings"
        addPreferencesFromResource(R.xml.pref_general)
    }
}