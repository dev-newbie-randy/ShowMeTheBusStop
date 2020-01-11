package kr.pe.randy.showmethebusstop

import android.app.SearchManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.commit
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import fr.arnaudguyon.xmltojsonlib.XmlToJson
import kr.pe.randy.showmethebusstop.data.BusStationData
import kr.pe.randy.showmethebusstop.fragment.BusRouteFragment
import kr.pe.randy.showmethebusstop.fragment.BusStationFragment
import kr.pe.randy.showmethebusstop.network.Parser
import kr.pe.randy.showmethebusstop.network.Request

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private val key by lazy {
        packageManager.getApplicationInfo(
            packageName,
            PackageManager.GET_META_DATA
        ).metaData.getString("service_key")!!
    }

    private var stationFragment: BusStationFragment? = null
    private var routeFragment: BusRouteFragment? = null

    private val volleyListener = Response.Listener<String> { response ->
        parseResponse(response)
    }

    private val volleyErrorListener = Response.ErrorListener  { error ->
        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
    }

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
        sendHttpRequest(Request.getBusStationListUrl(key, query))
        return false
    }

    override fun onQueryTextChange(newText: String) = false

    private fun sendHttpRequest(@NonNull url: String) {
        val requestQueue: RequestQueue = Volley.newRequestQueue(baseContext)
        object : StringRequest(Method.GET, url, volleyListener, volleyErrorListener) {
        }.apply {
            setShouldCache(false)
        }.also {
            requestQueue.add(it)
        }
    }

    private fun parseResponse(@NonNull response: String) {
        val receivedJSon = XmlToJson.Builder(response).build().toJson()!!
        val responseJSon = receivedJSon.getJSONObject("response")
        val msgBodyJSon = responseJSon.optJSONObject("msgBody")

        msgBodyJSon ?: run {
            supportFragmentManager.fragments[0].takeIf { fragment ->
                fragment is BusStationFragment
            }?.run {
                with(this as BusStationFragment) {
                    clearNoResult()
                    bindList(emptyList())
                }
            }
            return
        }

        msgBodyJSon?.optJSONArray("busStationList")?.let {
            supportFragmentManager.fragments[0].takeIf { fragment ->
                fragment is BusStationFragment
            }?.run {
                (this as BusStationFragment).bindList(Parser.getBusStationList(it))
            }
        }

        msgBodyJSon?.optJSONArray("busRouteList")?.let {
            supportFragmentManager.fragments[0].takeIf { fragment ->
                fragment is BusRouteFragment
            }?.run {
                (this as BusRouteFragment).bindList(Parser.getBusRouteList(it))
            }
        } ?: run {
            msgBodyJSon?.optJSONObject("busRouteList")?.let {
                supportFragmentManager.fragments[0].takeIf { fragment ->
                    fragment is BusRouteFragment
                }?.run {
                    (this as BusRouteFragment).bindList(Parser.getBusRouteList(it))
                }
            }
        }
    }

    private fun showBusStationFragment() {
        supportFragmentManager.commit(true) {
            stationFragment ?: run {
                stationFragment = BusStationFragment()
            }
            replace(R.id.fragment_container, stationFragment!!, BusStationFragment::javaClass.name)
        }
    }

    private fun showBusRouteFragment() {
        supportFragmentManager.commit(true) {
            routeFragment ?: run {
                routeFragment = BusRouteFragment()
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

    fun handleSelectedBusStation(busStation: BusStationData) {
        Toast.makeText(this, busStation.stationName + " " + busStation.stationId, Toast.LENGTH_SHORT).show()
        showBusRouteFragment()
        sendHttpRequest(Request.getBusListUrl(key, busStation.stationId))
    }
}