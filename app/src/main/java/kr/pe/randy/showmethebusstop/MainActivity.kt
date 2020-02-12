package kr.pe.randy.showmethebusstop

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kr.pe.randy.showmethebusstop.data.entity.BusStationData
import kr.pe.randy.showmethebusstop.view.ext.moveNext
import kr.pe.randy.showmethebusstop.view.ext.movePrev
import kr.pe.randy.showmethebusstop.view.ext.reduceDragSensitivity
import kr.pe.randy.showmethebusstop.view.fragment.BusRouteFragment
import kr.pe.randy.showmethebusstop.view.fragment.BusStationFragment
import kr.pe.randy.showmethebusstop.view.fragment.KinFragment
import kr.pe.randy.showmethebusstop.view.provider.StationSuggestionProvider

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var stationFragment: BusStationFragment
    private lateinit var routeFragment: BusRouteFragment
    private lateinit var kinFragment: KinFragment

    private var searchMenu: MenuItem? = null

    private val snackBar by lazy  {
        Snackbar.make(viewPager, R.string.exit, Snackbar.LENGTH_SHORT)
    }

    private val viewPager by lazy {
        findViewById<ViewPager2>(R.id.view_pager)
    }

    private val tabList by lazy {
        mutableListOf(
            Pair(resources.getString(R.string.tab_kin), KinFragment::class),
            Pair(resources.getString(R.string.tab_station), BusStationFragment::class),
            Pair(resources.getString(R.string.tab_route), BusRouteFragment::class))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_HOME or ActionBar.DISPLAY_SHOW_TITLE
            setIcon(R.drawable.baseline_directions_bus_24)
        }
        setupViewpager()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.takeIf {
            it.action == Intent.ACTION_SEARCH
        }?.run {
            getStringExtra(SearchManager.QUERY)?.let { query ->
                SearchRecentSuggestions(
                    this@MainActivity,
                    StationSuggestionProvider.AUTHORITY, StationSuggestionProvider.MODE
                ).saveRecentQuery(query, null)
                onQueryTextSubmit(query)
            }
        }
    }

    override fun onBackPressed() {
        if (snackBar.isShown) super.onBackPressed() else snackBar.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        searchMenu = menu.findItem(R.id.action_search).apply {
            (actionView as SearchView).apply {
                setSearchableInfo((getSystemService(Context.SEARCH_SERVICE) as SearchManager)
                    .getSearchableInfo(componentName))
                imeOptions = EditorInfo.IME_ACTION_SEARCH
                setOnQueryTextListener(this@MainActivity)
            }
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        stationFragment.searchStation(query)
        supportActionBar?.collapseActionView()
        return false
    }

    override fun onQueryTextChange(newText: String) = false

    private fun setupViewpager() {
        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return (tabList[position].second.constructors.first().call() as Fragment).apply {
                    when (this) {
                        is KinFragment -> kinFragment = this
                        is BusStationFragment -> stationFragment = this
                        is BusRouteFragment -> routeFragment = this
                    }
                }
            }
            override fun getItemCount() = tabList.size
        }

        viewPager.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                searchMenu?.apply {
                    isVisible = (position == FRAGMENT_STATION)
                }
                supportActionBar?.apply {
                    title = when (position) {
                        FRAGMENT_KIN -> resources.getString(R.string.actionbar_kin)
                        FRAGMENT_STATION -> resources.getString(R.string.actionbar_station)
                        FRAGMENT_ROUTE -> resources.getString(R.string.actionbar_route)
                        else -> ""
                    }
                }
            }
        })

        viewPager.reduceDragSensitivity()

        TabLayoutMediator(findViewById(R.id.tabs), viewPager) { tab, position ->
            tab.text = tabList[position].first
        }.attach()
    }

    fun handleSelectedBusStation(busStation: BusStationData, isDelete: Boolean = false) {
        with(viewPager) {
            when (currentItem) {
                FRAGMENT_KIN -> {
                    if (!isDelete) movePrev() else kinFragment.removeFromFavorite(busStation, true)
                }
                FRAGMENT_STATION -> moveNext()
                FRAGMENT_ROUTE -> moveNext()
            }

            post {
                if (currentItem == FRAGMENT_ROUTE) {
                    routeFragment.showRoute(busStation)
                } else if (currentItem == FRAGMENT_KIN  && !isDelete) {
                    kinFragment.addToFavorite(busStation, true)
                }
            }
        }
    }

    companion object {
        private const val FRAGMENT_KIN = 0
        private const val FRAGMENT_STATION = 1
        private const val FRAGMENT_ROUTE = 2
    }
}