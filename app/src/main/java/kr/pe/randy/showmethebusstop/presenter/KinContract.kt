package kr.pe.randy.showmethebusstop.presenter

import kr.pe.randy.showmethebusstop.data.entity.BusStationData
import kr.pe.randy.showmethebusstop.presenter.base.BasePresenter
import kr.pe.randy.showmethebusstop.presenter.base.BaseView

interface KinContract {
    interface View : BaseView {
        fun showFavorites(stationList : List<BusStationData>)
        fun notifyAdded()
        fun notifyRemoved()
    }

    interface Presenter : BasePresenter<View> {
        fun addToFavorite(station: BusStationData, needNotify: Boolean)
        fun removeFromFavorite(station: BusStationData, needNotify: Boolean)
        fun loadFavorites()
    }
}