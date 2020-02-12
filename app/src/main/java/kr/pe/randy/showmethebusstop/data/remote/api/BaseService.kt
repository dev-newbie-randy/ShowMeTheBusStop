package kr.pe.randy.showmethebusstop.data.remote.api

import kr.pe.randy.showmethebusstop.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

open class BaseService {
    companion object {
        private const val BASE_URL = "http://ws.bus.go.kr"
        const val KEY = "nHrki1yF4qXM1qaWVblXRlklor8rIGkROo1%2F2MegDQES28pGzF4ByMe%2BQud%2FvEnmfg2NF3mldeHq60KO5wBeEA%3D%3D"
    }

    private val client: OkHttpClient
        get() {
            return OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }).build()

        }

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
    }
}