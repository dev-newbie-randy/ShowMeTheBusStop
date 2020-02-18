package kr.pe.randy.showmethebusstop.data.remote.api

object RouteApiService : BaseService() {
    val api: RouteApiInterface = getRetrofit().create(RouteApiInterface ::class.java)
}