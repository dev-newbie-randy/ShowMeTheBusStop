package kr.pe.randy.showmethebusstop

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kr.pe.randy.showmethebusstop.model.BusStation
import kr.pe.randy.showmethebusstop.view.BusRouteFragment
import kr.pe.randy.showmethebusstop.view.BusStationFragment
import kr.pe.randy.showmethebusstop.view.KinFragment

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    companion object {
        private const val FRAGMENT_STATION = 0
        private const val FRAGMENT_ROUTE = 1
        private const val FRAGMENT_KIN = 2
    }

    private lateinit var stationFragment: BusStationFragment
    private lateinit var routeFragment: BusRouteFragment
    private lateinit var kinFragment: KinFragment

    private val tabLayout by lazy {
        findViewById<TabLayout>(R.id.tabs)
    }
    private val viewPager by lazy {
        findViewById<ViewPager2>(R.id.view_pager)
    }

    private val tabList by lazy {
        mutableListOf(
            Pair(resources.getString(R.string.tab_station), BusStationFragment::class),
            Pair(resources.getString(R.string.tab_route), BusRouteFragment::class),
            Pair(resources.getString(R.string.tab_kin), KinFragment::class)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_HOME
            setIcon(R.drawable.baseline_directions_bus_24)
        }

        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return tabList[position].second.constructors.first().call().apply {
                    when (this) {
                        is BusStationFragment -> stationFragment = this
                        is BusRouteFragment -> routeFragment = this
                        is KinFragment -> kinFragment = this
                    }
                }
            }
            override fun getItemCount() = tabList.size
        }

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabList[position].first
        }.attach()
    }

    override fun onStart() {
        super.onStart()
        viewPager.currentItem = FRAGMENT_STATION
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
        stationFragment.searchStation(query)
        return false
    }

    override fun onQueryTextChange(newText: String) = false

    private fun prepareSearch() {
        if (viewPager.currentItem == FRAGMENT_STATION) {
            stationFragment.clearNoResult()
        } else if (viewPager.currentItem == FRAGMENT_ROUTE) {
            viewPager.currentItem--
        }
    }

    fun handleSelectedBusStation(busStation: BusStation) {
        viewPager.currentItem++
        viewPager.post {
            if (viewPager.currentItem == FRAGMENT_ROUTE) {
                routeFragment.showRoute(busStation)
            } else if (viewPager.currentItem == FRAGMENT_KIN) {
                kinFragment.addToFavorite(busStation)
            }
        }
    }
}