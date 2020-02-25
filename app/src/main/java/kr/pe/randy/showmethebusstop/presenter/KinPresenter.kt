package kr.pe.randy.showmethebusstop.presenter

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.pe.randy.showmethebusstop.App
import kr.pe.randy.showmethebusstop.data.entity.BusStationData
import kr.pe.randy.showmethebusstop.data.repository.FavoriteRepository

class KinPresenter: KinContract.Presenter, ViewModel() {
    private var view : KinContract.View? = null
    private val repository = FavoriteRepository(App.DATABASE)

    override fun takeView(view: KinContract.View) {
        this.view = view.apply {
            repository.items.observe(getLifecycleOwner(), Observer { list ->
                showFavorites(list)
            })
        }
    }

    override fun dropView() {
        view = null
    }

    override fun addToFavorite(station: BusStationData, needNotify: Boolean) {
        viewModelScope.launch {
            view?.showProgress()
            withContext(Dispatchers.IO) {
                repository.addToFavorite(station)
            }
            view?.hideProgress()
            if (needNotify) {
                view?.notifyAdded()
            }
        }
    }

    override fun removeFromFavorite(station: BusStationData, needNotify: Boolean) {
        viewModelScope.launch {
            view?.showProgress()
            withContext(Dispatchers.IO) {
                repository.removeFromFavorite(station)
            }
            view?.hideProgress()
            if (needNotify) {
                view?.notifyRemoved()
            }
        }
    }

    override fun loadFavorites() {
        viewModelScope.launch {
            view?.showProgress()
            withContext(Dispatchers.IO) {
                repository.fetchFavoriteList()
            }
            view?.hideProgress()
        }
    }

    override fun reorderFavorites(stations: List<BusStationData>) {
        viewModelScope.launch {
            view?.showProgress()
            withContext(Dispatchers.IO) {
                repository.reorderFavorites(stations)
            }
            view?.hideProgress()
        }
    }
}