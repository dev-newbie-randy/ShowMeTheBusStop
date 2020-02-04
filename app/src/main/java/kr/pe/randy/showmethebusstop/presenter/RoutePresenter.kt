package kr.pe.randy.showmethebusstop.presenter

import kr.pe.randy.showmethebusstop.model.RouteApi
import kr.pe.randy.showmethebusstop.model.RouteData

class RoutePresenter : RouteContract.Presenter, RouteContract.Listener {
    private var searchView : RouteContract.View? = null

    override fun takeView(view: RouteContract.View) {
        searchView = view
    }

    override fun getRouteList(keyword: String) {
        searchView?.getContextIfAvailable()?.let {
            RouteApi.searchBusRoute(keyword, it, this@RoutePresenter)
        }
    }

    override fun dropView() {
        searchView = null
    }

    override fun onSuccess(list: List<RouteData>) {
        searchView?.showRouteList(list)
    }

    override fun onFail(msg: String) {
        searchView?.showError(msg)
    }
}