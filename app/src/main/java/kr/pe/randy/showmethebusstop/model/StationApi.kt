package kr.pe.randy.showmethebusstop.model

import androidx.annotation.NonNull
import kr.pe.randy.showmethebusstop.BuildConfig
import kr.pe.randy.showmethebusstop.presenter.StationContract
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object StationApi {
    private const val KEY = "nHrki1yF4qXM1qaWVblXRlklor8rIGkROo1%2F2MegDQES28pGzF4ByMe%2BQud%2FvEnmfg2NF3mldeHq60KO5wBeEA%3D%3D"

    interface StationSearchService {
        @GET("/ws/rest/busstationservice")
        fun setSearchParam(
            @Query("serviceKey", encoded = true) key: String,
            @Query("keyword", encoded = false) keyword: String
        ): Call<StationSearchResponse>
    }

    fun searchBusStation(@NonNull keyword: String, @NonNull listener: StationContract.Listener) {
        try {
            val client = OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }).build()

            val retrofit = Retrofit.Builder()
                .baseUrl("http://openapi.gbis.go.kr")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(client)
                .build()

            val service = retrofit.create(StationSearchService::class.java)
            val call = service.setSearchParam(KEY, keyword)

            call.enqueue(object : Callback<StationSearchResponse> {
                override fun onResponse(call: Call<StationSearchResponse>,
                                        response: retrofit2.Response<StationSearchResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.msgBody?.let {
                            listener.onSuccess(it.busStationList.toList())
                        }
                    }
                }

                override fun onFailure(call: Call<StationSearchResponse>, t: Throwable) {
                    listener.onFail(t.message!!)
                }
            })
        } catch (e: Exception) {
            listener.onFail(e.message!!)
        }
    }
}