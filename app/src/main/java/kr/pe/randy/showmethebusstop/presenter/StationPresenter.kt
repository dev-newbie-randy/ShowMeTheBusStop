package kr.pe.randy.showmethebusstop.presenter

import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.pe.randy.showmethebusstop.data.entity.BusStationData
import kr.pe.randy.showmethebusstop.data.remote.api.StationApiService
import kr.pe.randy.showmethebusstop.data.repository.StationRepository

class StationPresenter : StationContract.Presenter, ViewModel() {
    private var view: StationContract.View? = null
    private val repository = StationRepository(StationApiService.api)
    private val _items = MutableLiveData<MutableList<BusStationData>>()

    override fun takeView(@NonNull view: StationContract.View) {
        this.view = view.apply {
            _items.observe(getLifecycleOwner(), Observer { list ->
                showStationList(list)
            })
        }
    }

    override fun dropView() {
        view = null
    }

    override fun getStationList(@NonNull keyword: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val list = repository.fetchStationList(keyword)
                _items.postValue(list)
            }
        }
    }
}