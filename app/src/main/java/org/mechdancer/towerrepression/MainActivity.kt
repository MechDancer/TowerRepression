package org.mechdancer.towerrepression

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import org.mechdancer.towerrepression.scorer.ScorerFragment
import org.mechdancer.towerrepression.table.TableFragment
import org.mechdancer.towerrepression.timer.TimerFragment

class MainActivity : AppCompatActivity() {

    private lateinit var scorerFragment: ScorerFragment
    private lateinit var tableFragment: TableFragment
//    private lateinit var timerFragment: TimerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        scorerFragment = ScorerFragment()
        tableFragment = TableFragment()
//        timerFragment = TimerFragment()

        pager.adapter = PagerAdapter(supportFragmentManager, listOf(scorerFragment, tableFragment))
        pager.currentItem = 0

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
