package kr.pe.randy.showmethebusstop

import android.app.SearchManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import fr.arnaudguyon.xmltojsonlib.XmlToJson

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private val recyclerView by lazy {
        findViewById<RecyclerView>(R.id.recycler_view).apply {
            adapter = BusStationListAdapter()
            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
        }
    }

    private val busStopList = mutableListOf<BusStationData>()

    private val volleyListener = Response.Listener<String> { response ->
        val receivedJSon = XmlToJson.Builder(response).build().toJson()!!
        val responseJSon = receivedJSon.getJSONObject("response")
        val msgBodyJSon = responseJSon.getJSONObject("msgBody")
        val busStationListJson = msgBodyJSon.getJSONArray("busStationList")

        val length = busStationListJson.length() - 1

        for (i in 0..length) {
            val busStationJson = busStationListJson.getJSONObject(i)

            val busStopData = BusStationData(
                busStationJson.optString("centerYn"),
                busStationJson.optInt("districtCd"),
                busStationJson.optString("mobileNo"),
                busStationJson.optString("regionName"),
                busStationJson.optString("stationId"),
                busStationJson.optString("stationName"),
                busStationJson.optDouble("x"),
                busStationJson.optDouble("y")
            )
            busStopList.add(busStopData)
        }

        (recyclerView.adapter as BusStationListAdapter).setEntries(busStopList)
    }

    private val volleyErrorListener = Response.ErrorListener  { error ->
        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        val searchMenu = menu.findItem(R.id.action_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (searchMenu?.actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setOnQueryTextListener(this@MainActivity)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        sendHttpPostVolley(query)
        return false
    }

    override fun onQueryTextChange(newText: String) = false


    private fun sendHttpPostVolley(@NonNull keyword: String) {
        busStopList.clear()

        val key = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA).metaData.getString("service_key")

        val urlBuilder = StringBuilder("http://openapi.gbis.go.kr/ws/rest/busstationservice")
        urlBuilder.append("?")
        urlBuilder.append("serviceKey=")
        urlBuilder.append(key)
        urlBuilder.append("&")
        urlBuilder.append("keyword=")
        urlBuilder.append(keyword)

        val url = urlBuilder.toString()
        val requestQueue: RequestQueue = Volley.newRequestQueue(baseContext)

        object : StringRequest(Method.GET, url, volleyListener, volleyErrorListener) {
        }.apply {
            setShouldCache(false)
        }.also {
            requestQueue.add(it)
        }
    }
}