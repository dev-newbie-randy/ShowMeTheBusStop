package kr.pe.randy.showmethebusstop.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.pe.randy.showmethebusstop.App
import kr.pe.randy.showmethebusstop.model.data.FavoriteDatabase
import kr.pe.randy.showmethebusstop.model.data.FavoriteStation
import kr.pe.randy.showmethebusstop.presenter.KinContract

class FavoriteApi {
    companion object {
        val INSTANCE by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            FavoriteApi()
        }
    }

    private val dataBase: FavoriteDatabase = App.DATABASE
    private val _itemList = MutableLiveData<List<BusStation>>()
    val itemList : LiveData<List<BusStation>>
        get() = _itemList

    private fun convertData(newItem: BusStation) = with(newItem) {
        FavoriteStation(stationId, mobileNo, stationName)
    }

    private fun convertData(favorite: FavoriteStation) = with(favorite) {
        BusStation(stationId = stationId, mobileNo = stationNumber, stationName = stationName)
    }

    fun insertStation(station: BusStation, listener: KinContract.Listener) {
        _itemList.value?.find {
            it.stationId == station.stationId
        }?.run {
            listener.onFail()
            return
        }

        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val dao = dataBase.favoriteDao()
                dao.insert(convertData(station))
            }
            withContext(Dispatchers.Main) {
                loadStations()
                listener.onSuccess()
            }
        }
    }

    fun deleteStation(station: BusStation) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val dao = dataBase.favoriteDao()
                dao.delete(convertData(station))
            }
            withContext(Dispatchers.Main) {
                loadStations()
            }
        }
    }

    fun loadStations() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val list = mutableListOf<BusStation>()
                dataBase.favoriteDao().load().forEach { favorite ->
                    list.add(convertData(favorite))
                }
                _itemList.postValue(list)
            }
        }
    }
}