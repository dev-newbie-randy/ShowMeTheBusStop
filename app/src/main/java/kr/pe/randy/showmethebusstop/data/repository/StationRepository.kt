package kr.pe.randy.showmethebusstop.data.repository

import android.util.Log
import kr.pe.randy.showmethebusstop.data.entity.BusStationData
import kr.pe.randy.showmethebusstop.data.entity.RouteData
import kr.pe.randy.showmethebusstop.data.remote.NetworkResult
import kr.pe.randy.showmethebusstop.data.remote.api.BaseService
import kr.pe.randy.showmethebusstop.data.remote.api.StationApiInterface

class StationRepository(private val apiInterface: StationApiInterface) : BaseRepository() {

    suspend fun fetchStationList(keyword: String): MutableList<BusStationData> {
        val searchKeyword = keyword.replace("-", "")
        var number = -1
        try {
            number = Integer.valueOf(searchKeyword)
        } catch (e: NumberFormatException) {

        }

        var output = mutableListOf<BusStationData>()

        if (number > 0) {
            val resultById = apiOutput(
                call = { apiInterface.fetchStationById(BaseService.KEY, searchKeyword) },
                error = "calling fetchUserList failed"
            )
            when (resultById) {
                is NetworkResult.Success ->
                    resultById.output.msgBody?.run {
                        output.add(convertData(routeInfoList[0]))
                    }
                is NetworkResult.Error -> Log.d("Error", "${resultById.exception}")
            }
        } else {
            val resultByName = apiOutput(
                call = { apiInterface.fetchStation(BaseService.KEY, keyword) },
                error = "calling fetchUserList failed"
            )
            when (resultByName) {
                is NetworkResult.Success ->
                    resultByName.output.msgBody?.run {
                        output = busStationList
                    }
                is NetworkResult.Error -> Log.d("Error", "${resultByName.exception}")
            }
        }

        return output
    }

    private fun convertData(route: RouteData) = with(route) {
        BusStationData(
            stationId = stId,
            mobileNo = arsId,
            stationName = stNm
        )
    }
}