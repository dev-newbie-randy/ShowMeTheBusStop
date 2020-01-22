package kr.pe.randy.showmethebusstop.presenter

import kr.pe.randy.showmethebusstop.model.BusStation

interface SearchContract {

    interface View : BaseView {
        fun showStationList(stationList : List<BusStation>)
    }

    interface Presenter : BasePresenter<View> {
        fun getStationList()
    }
}