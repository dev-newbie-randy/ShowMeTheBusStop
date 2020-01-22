package kr.pe.randy.showmethebusstop.presenter

import kr.pe.randy.showmethebusstop.model.BusStation
import kr.pe.randy.showmethebusstop.model.BusStationApi

class SearchPresenter : SearchContract.Presenter, SearchContract.Listener {
    private var searchView : SearchContract.View? = null

    override fun takeView(view: SearchContract.View) {
        searchView = view
    }

    override fun getStationList(keyword: String) {
        BusStationApi.searchBusStation(keyword, this)
    }

    override fun dropView() {
        searchView = null
    }

    override fun onSuccess(list: List<BusStation>) {
        searchView?.showStationList(list)
    }

    override fun onFail(msg: String) {
        searchView?.showError(msg)
    }
}