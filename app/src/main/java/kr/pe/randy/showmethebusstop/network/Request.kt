package kr.pe.randy.showmethebusstop.network

import androidx.annotation.NonNull

object Request {

    fun getBusStationListUrl(@NonNull key: String, @NonNull keyword: String) : String {
        val urlBuilder = StringBuilder("http://openapi.gbis.go.kr/ws/rest/busstationservice")
        urlBuilder.append("?")
        urlBuilder.append("serviceKey=")
        urlBuilder.append(key)
        urlBuilder.append("&")
        urlBuilder.append("keyword=")
        urlBuilder.append(keyword)
        return urlBuilder.toString()
    }

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