package kr.pe.randy.showmethebusstop.presenter

import kr.pe.randy.showmethebusstop.model.BusStation

interface StationContract {

    interface View : BaseView {
        fun showStationList(stationList : List<BusStation>)
    }

    interface Presenter : BasePresenter<View> {
        fun getStationList(keyword: String)
    }

    interface Listener {
        fun onSuccess(list: List<BusStation>)
        fun onFail(msg: String)
    }
}