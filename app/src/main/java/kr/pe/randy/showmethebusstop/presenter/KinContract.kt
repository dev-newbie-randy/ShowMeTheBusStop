package kr.pe.randy.showmethebusstop.presenter

import androidx.lifecycle.LifecycleOwner
import kr.pe.randy.showmethebusstop.model.BusStation

interface KinContract {
    interface View : BaseView {
        fun showFavorites(stationList : List<BusStation>)
        fun getLifecycleOwner(): LifecycleOwner
        fun notifyAdded()
    }

    interface Presenter : BasePresenter<View> {
        fun addToFavorite(station: BusStation)
        fun removeFromFavorite(station: BusStation)
        fun loadFavorites()
    }

    interface Listener {
        fun onSuccess()
        fun onFail()
    }
}