package kr.pe.randy.showmethebusstop.data.remote.api

import kr.pe.randy.showmethebusstop.data.entity.RouteInfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RouteApiInterface {
    @GET("/api/rest/stationinfo/getStationByUid")
    suspend fun fetchRoute(
        @Query("serviceKey", encoded = true) key: String,
        @Query("arsId", encoded = false) stationId: String
    ): Response<RouteInfoResponse>
}