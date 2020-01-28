package kr.pe.randy.showmethebusstop.model

import android.content.Context
import androidx.annotation.NonNull
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kr.pe.randy.showmethebusstop.presenter.RouteContract

object RouteApi {

    private const val KEY = "nHrki1yF4qXM1qaWVblXRlklor8rIGkROo1%2F2MegDQES28pGzF4ByMe%2BQud%2FvEnmfg2NF3mldeHq60KO5wBeEA%3D%3D"
    private lateinit var listener: RouteContract.Listener

    private val volleyListener = Response.Listener<String> { response ->
        val routeList = RouteParser.parseResponse(response.toString())
        if (routeList.isEmpty()) {
            listener.onFail("NO RESULT")
        }
        listener.onSuccess(routeList)
    }

    private val volleyErrorListener = Response.ErrorListener  { error ->
        listener.onFail(error.message!!)
    }

    fun searchBusRoute(@NonNull busStationId: String, @NonNull context: Context, @NonNull listener: RouteContract.Listener) {
        this.listener = listener
        sendHttpRequest(getBusRouteListUrl(busStationId), context)
    }

    private fun sendHttpRequest(@NonNull url: String, @NonNull context: Context) {
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        object : StringRequest(Method.GET, url, volleyListener, volleyErrorListener) {
        }.apply {
            setShouldCache(false)
        }.also {
            requestQueue.add(it)
        }
    }

    private fun getBusRouteListUrl(@NonNull busStationId: String) : String {
        val urlBuilder = StringBuilder("http://openapi.gbis.go.kr/ws/rest/busstationservice/route")
        urlBuilder.append("?")
        urlBuilder.append("serviceKey=")
        urlBuilder.append(KEY)
        urlBuilder.append("&")
        urlBuilder.append("stationId=")
        urlBuilder.append(busStationId)
        return urlBuilder.toString()
    }
}