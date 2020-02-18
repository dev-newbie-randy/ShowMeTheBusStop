package kr.pe.randy.showmethebusstop.data.repository

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.withTransaction
import kr.pe.randy.showmethebusstop.data.entity.BusStationData
import kr.pe.randy.showmethebusstop.data.local.api.FavoriteApi

class FavoriteRepository(private val database: RoomDatabase) {
    private val api = FavoriteApi.INSTANCE
    private val _items = MutableLiveData<List<BusStationData>>()
    val items : LiveData<List<BusStationData>>
        get() = _items

    @Transaction
    suspend fun addToFavorite(@NonNull station: BusStationData) {
        database.withTransaction {
            api.insertStation(station).takeIf {
                it
            }?.apply {
                fetchFavoriteList()
            }
        }
    }

    @Transaction
    suspend fun removeFromFavorite(@NonNull station: BusStationData) {
        database.withTransaction {
            api.deleteStation(station).takeIf {
                it
            }?.apply {
                fetchFavoriteList()
            }
        }
    }

    suspend fun fetchFavoriteList() {
        database.withTransaction {
            _items.postValue(api.loadFavorites())
        }
    }
}