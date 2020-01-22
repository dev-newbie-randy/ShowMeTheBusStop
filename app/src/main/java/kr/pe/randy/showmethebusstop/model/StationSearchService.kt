package kr.pe.randy.showmethebusstop.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface StationSearchService {
    @GET("/ws/rest/busstationservice")
    fun setSearchParam(
        @Query("serviceKey", encoded = true) key: String,
        @Query("keyword", encoded = false) keyword: String
    ): Call<StationSearchResponse>
}