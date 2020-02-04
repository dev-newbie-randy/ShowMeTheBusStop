package kr.pe.randy.showmethebusstop.presenter

import kr.pe.randy.showmethebusstop.model.BusStation
import kr.pe.randy.showmethebusstop.model.StationApi

class StationPresenter : StationContract.Presenter, StationContract.Listener {
    private var searchView : StationContract.View? = null

    override fun takeView(view: StationContract.View) {
        searchView = view
    }

    override fun getStationList(keyword: String) {
        StationApi.searchBusStation(keyword, this)
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