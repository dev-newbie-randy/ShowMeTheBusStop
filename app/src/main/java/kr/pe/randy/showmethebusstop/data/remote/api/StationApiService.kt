package kr.pe.randy.showmethebusstop.data.remote.api

object StationApiService: BaseService() {
    val api: StationApiInterface = getRetrofit().create(StationApiInterface ::class.java)
}