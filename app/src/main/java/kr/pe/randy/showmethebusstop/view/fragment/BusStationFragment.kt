package kr.pe.randy.showmethebusstop.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
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
    private lateinit var fragmentView: View

    private val recyclerView by lazy {
        fragmentView.findViewById<RecyclerView>(R.id.recycler_view).apply {
            adapter = BusStationListAdapter()
                    .apply {
                        onItemClick = {
                            onBusStationClick(it)
                        }
                    }
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }
    }

    private val noResultView by lazy {
        fragmentView.findViewById<TextView>(R.id.no_result)
    }

    private val presenter = StationPresenter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_station, container, false).apply {
            fragmentView = this
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.takeView(this)
    }

    fun searchStation(keyword: String) {
        noResultView.visibility = View.GONE
        presenter.getStationList(keyword)
    }

    // SearchContract.View
    override fun showStationList(stationList : List<BusStationData>) {
        if (stationList.isEmpty()) {
            noResultView.visibility = View.VISIBLE
        }
        (recyclerView.adapter as BusStationListAdapter).setEntries(stationList)
    }

    override fun getLifecycleOwner(): LifecycleOwner {
        return viewLifecycleOwner
    }

    override fun showError(error : String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    override fun showProgress() {
        fragmentView.findViewById<ContentLoadingProgressBar>(R.id.progress).visibility = View.VISIBLE
    }

    override fun hideProgress() {
        fragmentView.findViewById<ContentLoadingProgressBar>(R.id.progress).visibility = View.GONE
    }
    //

    private fun onBusStationClick(data: BusStationData) {
        (activity as? MainActivity)?.handleSelectedBusStation(data)
    }
}