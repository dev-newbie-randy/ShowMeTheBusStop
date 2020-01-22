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
import kr.pe.randy.showmethebusstop.fragment.BusRouteFragment
import kr.pe.randy.showmethebusstop.fragment.BusStationFragment
import kr.pe.randy.showmethebusstop.network.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

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
        sendHttpPostRetroFit(query)
        return false
    }

    override fun onQueryTextChange(newText: String) = false

    // 버스 노선 검색은 volley 으로 하고
    private fun sendHttpRequest(@NonNull url: String) {
        val requestQueue: RequestQueue = Volley.newRequestQueue(baseContext)
        object : StringRequest(Method.GET, url, volleyListener, volleyErrorListener) {
        }.apply {
            setShouldCache(false)
        }.also {
            requestQueue.add(it)
        }
    }

    // 버스 정류장 검색은 retrofit 으로 하고
    private fun sendHttpPostRetroFit(@NonNull keyword: String) {
        try {
            val client = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()

            val retrofit = Retrofit.Builder()
                .baseUrl("http://openapi.gbis.go.kr")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(client)
                .build()

            val service = retrofit.create(StationSearchService::class.java)
            val call = service.setSearchParam(key, keyword)

            call.enqueue(object : Callback<StationSearchResponse> {
                override fun onResponse(call: Call<StationSearchResponse>,
                                        response: retrofit2.Response<StationSearchResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            it.msgBody?.let { msgBody ->
                                supportFragmentManager.fragments[0].takeIf { fragment ->
                                    fragment is BusStationFragment
                                }?.run {
                                    (this as BusStationFragment).bindList(msgBody.busStationList.toList())
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<StationSearchResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
            Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
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

        msgBodyJSon.optJSONArray("busRouteList")?.let {
            supportFragmentManager.fragments[0].takeIf { fragment ->
                fragment is BusRouteFragment
            }?.run {
                (this as BusRouteFragment).bindList(Parser.getBusRouteList(it))
            }
        } ?: run {
            msgBodyJSon.optJSONObject("busRouteList")?.let {
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

    fun handleSelectedBusStation(busStation: BusStation) {
        Toast.makeText(this, busStation.stationName + " " + busStation.stationId, Toast.LENGTH_SHORT).show()
        showBusRouteFragment()
        sendHttpRequest(Request.getBusListUrl(key, busStation.stationId))
    }
}