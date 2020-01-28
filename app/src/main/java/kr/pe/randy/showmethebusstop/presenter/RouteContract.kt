package kr.pe.randy.showmethebusstop.presenter

import android.content.Context
import kr.pe.randy.showmethebusstop.model.RouteData

interface RouteContract {
    interface View : BaseView {
        fun showRouteList(stationList : List<RouteData>)
        fun getContextIfAvailable(): Context?
    }

    interface Presenter : BasePresenter<View> {
        fun getRouteList(keyword: String)
    }

    interface Listener {
        fun onSuccess(list: List<RouteData>)
        fun onFail(msg: String)
    }
}