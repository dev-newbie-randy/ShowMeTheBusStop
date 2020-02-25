package kr.pe.randy.showmethebusstop.data.local.api

import kr.pe.randy.showmethebusstop.App
import kr.pe.randy.showmethebusstop.data.entity.BusStationData
import kr.pe.randy.showmethebusstop.data.entity.FavoriteStation
import kr.pe.randy.showmethebusstop.data.local.database.FavoriteDatabase

class FavoriteApi {
    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            FavoriteApi()
        }
    }

    private val dataBase: FavoriteDatabase = App.DATABASE
    private val stationList = mutableListOf<BusStationData>()

    private fun convertData(newItem: BusStationData) = with(newItem) {
        FavoriteStation(stationId, mobileNo, stationName)
    }

    private fun convertData(favorite: FavoriteStation) = with(favorite) {
        BusStationData(
            stationId = stationId,
            mobileNo = stationNumber,
            stationName = stationName
        )
    }

    suspend fun insertStation(station: BusStationData): Boolean {
        stationList.find {
            it.stationId == station.stationId
        }?.run {
            return false
        }

        val dao = dataBase.favoriteDao()
        dao.insert(convertData(station))
        return true
    }

    suspend fun insertAll(stations: List<BusStationData>) {
        val favorites = mutableListOf<FavoriteStation>()
        for (station in stations) {
            favorites.add(convertData(station))
        }
        val dao = dataBase.favoriteDao()
        dao.insertAll(favorites)
    }

    suspend fun deleteStation(station: BusStationData): Boolean {
        if (stationList.size == 0) {
            return false
        }
        val dao = dataBase.favoriteDao()
        dao.delete(convertData(station))
        return true
    }

    suspend fun deleteAll() {
        val dao = dataBase.favoriteDao()
        dao.deleteAll()
    }

    suspend fun loadFavorites(): List<BusStationData> {
        stationList.clear()
        dataBase.favoriteDao().load().map { favorite ->
            stationList.add(convertData(favorite))
        }
        return stationList
    }
}