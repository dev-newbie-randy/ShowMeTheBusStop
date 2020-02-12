package kr.pe.randy.showmethebusstop.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kr.pe.randy.showmethebusstop.MainActivity
import kr.pe.randy.showmethebusstop.R
import kr.pe.randy.showmethebusstop.data.entity.BusStationData
import kr.pe.randy.showmethebusstop.presenter.StationContract
import kr.pe.randy.showmethebusstop.presenter.StationPresenter
import kr.pe.randy.showmethebusstop.view.adapter.BusStationListAdapter

class BusStationFragment : Fragment(), StationContract.View {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noResultView: TextView
    private lateinit var presenter: StationPresenter

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
        presenter = StationPresenter()
        presenter.takeView(this)
    }

    fun searchStation(keyword: String) {
        noResultView.visibility = View.GONE
        presenter.getStationList(keyword)
    }

    override fun getLifecycleOwner(): LifecycleOwner {
        return viewLifecycleOwner
    }

    // SearchContract.View
    override fun showStationList(stationList : List<BusStationData>) {
        if (stationList.isEmpty()) {
            noResultView.visibility = View.VISIBLE
        }
        (recyclerView.adapter as BusStationListAdapter).setEntries(stationList)
    }

    // SearchContract.View
    override fun showError(error : String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    private fun onBusStationClick(data: BusStationData) {
        (activity as? MainActivity)?.handleSelectedBusStation(data)
    }
}