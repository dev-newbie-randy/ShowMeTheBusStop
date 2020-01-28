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
import kr.pe.randy.showmethebusstop.presenter.SearchContract
import kr.pe.randy.showmethebusstop.presenter.SearchPresenter

class BusStationFragment : Fragment(), SearchContract.View {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noResultView: TextView
    private lateinit var searchPresenter: SearchPresenter

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
        searchPresenter = SearchPresenter()
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

    fun bindList(list: List<BusStation>) {
        if (list.isEmpty()) {
            noResultView.visibility = View.VISIBLE
        }
        (recyclerView.adapter as BusStationListAdapter).setEntries(list)
    }

    private fun onBusStationClick(data: BusStation) {
        (activity as? MainActivity)?.handleSelectedBusStation(data)
    }

}