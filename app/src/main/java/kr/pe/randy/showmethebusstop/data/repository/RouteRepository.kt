package kr.pe.randy.showmethebusstop.data.repository

import android.util.Log
import kr.pe.randy.showmethebusstop.data.entity.RouteData
import kr.pe.randy.showmethebusstop.data.remote.NetworkResult
import kr.pe.randy.showmethebusstop.data.remote.api.BaseService
import kr.pe.randy.showmethebusstop.data.remote.api.RouteApiInterface

class RouteRepository(private val apiInterface: RouteApiInterface) : BaseRepository() {
    suspend fun fetchRouteList(stationId: String): MutableList<RouteData> {
        val result = apiOutput(
            call = { apiInterface.fetchRoute(BaseService.KEY, stationId) },
            error = "calling fetchRouteList failed"
        )
        var output = mutableListOf<RouteData>()

        when (result) {
            is NetworkResult.Success ->
                result.output.msgBody?.run {
                    output = routeInfoList
                }
            is NetworkResult.Error ->
                Log.d("Error", "${result.exception}")
        }
        return output
    }
}