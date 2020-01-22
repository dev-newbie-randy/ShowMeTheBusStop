package kr.pe.randy.showmethebusstop.model

import androidx.annotation.NonNull
import kr.pe.randy.showmethebusstop.presenter.SearchContract
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

object BusStationApi {
    // https://github.com/android/architecture-samples/tree/todo-mvp
    private const val KEY = "nHrki1yF4qXM1qaWVblXRlklor8rIGkROo1%2F2MegDQES28pGzF4ByMe%2BQud%2FvEnmfg2NF3mldeHq60KO5wBeEA%3D%3D"

    fun searchBusStation(@NonNull keyword: String, @NonNull listener: SearchContract.Listener) {
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
            val call = service.setSearchParam(KEY, keyword)

            call.enqueue(object : Callback<StationSearchResponse> {
                override fun onResponse(call: Call<StationSearchResponse>,
                                        response: retrofit2.Response<StationSearchResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            it.msgBody?.let { msgBody ->
                                listener.onSuccess(msgBody.busStationList.toList())
                            }
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