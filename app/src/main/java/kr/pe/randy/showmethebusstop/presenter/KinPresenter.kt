package kr.pe.randy.showmethebusstop.presenter

import androidx.lifecycle.Observer
import kr.pe.randy.showmethebusstop.model.BusStation
import kr.pe.randy.showmethebusstop.model.FavoriteApi

class KinPresenter: KinContract.Presenter, KinContract.Listener {
    private var view : KinContract.View? = null

    override fun addToFavorite(station: BusStation) {
        FavoriteApi.INSTANCE.insertStation(station, this)
    }

    override fun removeFromFavorite(station: BusStation) {
        FavoriteApi.INSTANCE.deleteStation(station)
    }

    override fun loadFavorites() {
        if (!FavoriteApi.INSTANCE.itemList.hasObservers()) {
            view?.let {
                FavoriteApi.INSTANCE.itemList.observe(it.getLifecycleOwner(), Observer { list ->
                    it.showFavorites(list)
                })
            }
        }

        FavoriteApi.INSTANCE.loadStations()
    }

    override fun takeView(view: KinContract.View) {
        this.view = view
    }
    override fun dropView() {
        view = null
    }

    override fun onSuccess() {
        view?.notifyAdded()
    }

    override fun onFail() {
        view?.showError("")
    }
}