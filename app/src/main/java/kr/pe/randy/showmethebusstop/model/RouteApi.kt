package kr.pe.randy.showmethebusstop.model

import androidx.annotation.NonNull

object RouteApi {

    fun getBusListUrl(@NonNull key: String, @NonNull busStationId: String) : String {
        val urlBuilder = StringBuilder("http://openapi.gbis.go.kr/ws/rest/busstationservice/route")
        urlBuilder.append("?")
        urlBuilder.append("serviceKey=")
        urlBuilder.append(key)
        urlBuilder.append("&")
        urlBuilder.append("stationId=")
        urlBuilder.append(busStationId)
        return urlBuilder.toString()
    }
}