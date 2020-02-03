package kr.pe.randy.showmethebusstop.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kr.pe.randy.showmethebusstop.MainActivity
import kr.pe.randy.showmethebusstop.R
import kr.pe.randy.showmethebusstop.model.BusStation
import kr.pe.randy.showmethebusstop.presenter.BusStationListAdapter
import kr.pe.randy.showmethebusstop.presenter.StationContract
import kr.pe.randy.showmethebusstop.presenter.StationPresenter

class BusStationFragment : Fragment(), StationContract.View {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noResultView: TextView
    private lateinit var searchPresenter: StationPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_station, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            adapter = BusStationListAdapter()
                .apply {
                onItemClick = {
                    onBusStationClick(it)
                }
            }
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }

        noResultView = view.findViewById(R.id.no_result)
        initPresenter()
    }

    private fun initPresenter() {
        searchPresenter = StationPresenter()
        searchPresenter.takeView(this)
    }

    fun searchStation(keyword: String) {
        searchPresenter.getStationList(keyword)
    }

    // SearchContract.View
    override fun showStationList(stationList : List<BusStation>) {
        if (stationList.isEmpty()) {
            noResultView.visibility = View.VISIBLE
        }
        (recyclerView.adapter as BusStationListAdapter).setEntries(stationList)
    }

    // SearchContract.View
    override fun showError(error : String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    fun clearNoResult() {
        noResultView.visibility = View.GONE
    }

    private fun onBusStationClick(data: BusStation) {
        (activity as? MainActivity)?.handleSelectedBusStation(data)
    }

    override fun toString(): String {
        return "정류장 검색"
    }

    companion object {
        fun create(): BusStationFragment {
            return BusStationFragment()
        }
    }
}