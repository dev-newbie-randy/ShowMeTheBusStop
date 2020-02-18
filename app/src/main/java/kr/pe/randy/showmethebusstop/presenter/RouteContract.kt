package kr.pe.randy.showmethebusstop.presenter

import kr.pe.randy.showmethebusstop.data.entity.RouteData
import kr.pe.randy.showmethebusstop.presenter.base.BasePresenter
import kr.pe.randy.showmethebusstop.presenter.base.BaseView

interface RouteContract {
    interface View : BaseView {
        fun showRouteList(stationList : List<RouteData>)
    }

    interface Presenter : BasePresenter<View> {
        fun getRouteList(stationId: String)
    }
}