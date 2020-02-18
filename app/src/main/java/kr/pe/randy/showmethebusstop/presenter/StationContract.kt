package kr.pe.randy.showmethebusstop.presenter

import kr.pe.randy.showmethebusstop.data.entity.BusStationData
import kr.pe.randy.showmethebusstop.presenter.base.BasePresenter
import kr.pe.randy.showmethebusstop.presenter.base.BaseView

interface StationContract {

    interface View : BaseView {
        fun showStationList(stationList : List<BusStationData>)
    }

    interface Presenter : BasePresenter<View> {
        fun getStationList(keyword: String)
    }
}