package org.mechdancer.towerrepression

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.mechdancer.towerrepression.event.ClearEvent
import org.mechdancer.towerrepression.preference.SettingsFragment
import org.mechdancer.towerrepression.scorer.ScorerFragment
import org.mechdancer.towerrepression.table.TableFragment

class MainActivity : AppCompatActivity() {

    private lateinit var scorerFragment: ScorerFragment
    private lateinit var tableFragment: TableFragment
    private lateinit var settingsFragment: SettingsFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        scorerFragment = ScorerFragment()
        tableFragment = TableFragment()
        settingsFragment = SettingsFragment()

        pager.adapter = PagerAdapter(supportFragmentManager, listOf(settingsFragment, scorerFragment, tableFragment))
        pager.currentItem = 1
        pager.offscreenPageLimit = 4

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                pager.currentItem = 0
                true
            }
            R.id.action_clear -> {
                Snackbar.make(pager, "清空场地", Snackbar.LENGTH_SHORT).show()
                EventBus.getDefault().post(ClearEvent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
