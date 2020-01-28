package kr.pe.randy.showmethebusstop.model

import androidx.annotation.NonNull
import fr.arnaudguyon.xmltojsonlib.XmlToJson
import org.json.JSONArray
import org.json.JSONObject

object RouteParser {

    fun parseResponse(@NonNull response: String) : MutableList<RouteData> {
        val receivedJSon = XmlToJson.Builder(response).build().toJson()!!
        val responseJSon = receivedJSon.getJSONObject("response")
        val msgBodyJSon = responseJSon.optJSONObject("msgBody")

        msgBodyJSon ?: run {
            return mutableListOf()
        }

        msgBodyJSon.optJSONArray("busRouteList")?.let {
            return getBusRouteList(it)
        } ?: run {
            msgBodyJSon.optJSONObject("busRouteList")?.let {
                return getBusRouteList(it)
            }
        }

        return mutableListOf()
    }

    private fun getBusRouteList(busRouteListJson: JSONArray): MutableList<RouteData> {
        val busRouteList = mutableListOf<RouteData>()

        val length = busRouteListJson.length() - 1

        for (i in 0..length) {
            val busRouteJson = busRouteListJson.getJSONObject(i)

            val busRouteData =
                RouteData(
                    busRouteJson.optInt("staOrder"),
                    busRouteJson.optString("routeId"),
                    busRouteJson.optInt("districtCd"),
                    busRouteJson.optString("regionName"),
                    busRouteJson.optString("routeTypeName"),
                    busRouteJson.optString("routeTypeCd"),
                    busRouteJson.optString("routeName")
                )

            busRouteList.add(busRouteData)
        }
        return busRouteList
    }

    private fun getBusRouteList(busRouteJson: JSONObject): MutableList<RouteData> {
        val busRouteList = mutableListOf<RouteData>()

        val busRouteData = RouteData(
            busRouteJson.optInt("staOrder"),
            busRouteJson.optString("routeId"),
            busRouteJson.optInt("districtCd"),
            busRouteJson.optString("regionName"),
            busRouteJson.optString("routeTypeName"),
            busRouteJson.optString("routeTypeCd"),
            busRouteJson.optString("routeName")
        )

        busRouteList.add(busRouteData)
        return busRouteList
    }
//
//    fun convertId(rawId: String): String {
//        val id = rawId.trim()
//        if (id.isNotEmpty()) {
//            val builder = StringBuilder(id.substring(0, 2))
//            builder.append("-")
//            builder.append(id.substring(2, id.length))
//            return builder.toString()
//        }
//        return id
//    }
}