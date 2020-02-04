package kr.pe.randy.showmethebusstop

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.commit
import kr.pe.randy.showmethebusstop.model.BusStation
import kr.pe.randy.showmethebusstop.view.BusRouteFragment
import kr.pe.randy.showmethebusstop.view.BusStationFragment

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private var stationFragment: BusStationFragment? = null
    private var routeFragment: BusRouteFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_HOME
            setIcon(R.drawable.baseline_directions_bus_24)
        }
    }

    override fun onStart() {
        super.onStart()
        showBusStationFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        val searchMenu = menu.findItem(R.id.action_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (searchMenu?.actionView as SearchView).apply {
            queryHint = getString(R.string.search_hint)
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setOnQueryTextListener(this@MainActivity)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        prepareSearch()
        stationFragment?.searchStation(query)
        return false
    }

    override fun onQueryTextChange(newText: String) = false

    private fun showBusStationFragment() {
        supportFragmentManager.commit(true) {
            stationFragment ?: run {
                stationFragment =
                    BusStationFragment()
            }
            replace(R.id.fragment_container, stationFragment!!, BusStationFragment::javaClass.name)
        }
    }

    private fun showBusRouteFragment() {
        supportFragmentManager.commit(true) {
            routeFragment ?: run {
                routeFragment =
                    BusRouteFragment()
            }
            replace(R.id.fragment_container, routeFragment!!, BusRouteFragment::javaClass.name)
        }
    }

    private fun prepareSearch() {
        val fragment = supportFragmentManager.fragments[0]
        if (fragment is BusStationFragment) {
            fragment.clearNoResult()
        } else if (fragment is BusRouteFragment) {
            showBusStationFragment()
        }
    }

    fun handleSelectedBusStation(busStation: BusStation) {
        Toast.makeText(this, busStation.stationName + " " + busStation.stationId, Toast.LENGTH_SHORT).show()
        showBusRouteFragment()
        Handler(Looper.getMainLooper()).postDelayed({
            (routeFragment?.searchRoute(busStation.stationId))
        }, 100L)
    }
}