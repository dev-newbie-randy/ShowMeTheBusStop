package kr.pe.randy.showmethebusstop.data.remote.api

import kr.pe.randy.showmethebusstop.data.entity.RouteInfoResponse
import kr.pe.randy.showmethebusstop.data.entity.StationSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface StationApiInterface {
    @GET("/api/rest/stationinfo/getStationByName")
    suspend fun fetchStation(
        @Query("serviceKey", encoded = true) key: String,
        @Query("stSrch", encoded = false) keyword: String
    ): Response<StationSearchResponse>

    @GET("/api/rest/stationinfo/getStationByUid")
    suspend fun fetchStationById(
        @Query("serviceKey", encoded = true) key: String,
        @Query("arsId", encoded = false) stationId: String
    ): Response<RouteInfoResponse>
}