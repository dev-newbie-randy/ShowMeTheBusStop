package kr.pe.randy.showmethebusstop.presenter

import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.pe.randy.showmethebusstop.data.entity.RouteData
import kr.pe.randy.showmethebusstop.data.remote.api.RouteApiService
import kr.pe.randy.showmethebusstop.data.repository.RouteRepository

class RoutePresenter : RouteContract.Presenter, ViewModel() {
    private var view : RouteContract.View? = null
    private val repository = RouteRepository(RouteApiService.api)
    private val _items = MutableLiveData<MutableList<RouteData>>()

    override fun takeView(view: RouteContract.View) {
        this.view = view.apply {
            _items.observe(getLifecycleOwner(), Observer { list ->
                showRouteList(list)
            })
        }
    }

    override fun dropView() {
        view = null
    }

    override fun getRouteList(@NonNull stationId: String) {
        viewModelScope.launch {
            view?.showProgress()
            withContext(Dispatchers.Default) {
                val list = repository.fetchRouteList(stationId.trim())
                _items.postValue(list)
            }
            view?.hideProgress()
        }
    }
}