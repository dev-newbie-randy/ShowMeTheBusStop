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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kr.pe.randy.showmethebusstop.MainActivity
import kr.pe.randy.showmethebusstop.R
import kr.pe.randy.showmethebusstop.data.entity.BusStationData
import kr.pe.randy.showmethebusstop.data.entity.RouteData
import kr.pe.randy.showmethebusstop.presenter.RouteContract
import kr.pe.randy.showmethebusstop.presenter.RoutePresenter
import kr.pe.randy.showmethebusstop.view.adapter.BusRouteListAdapter

class BusRouteFragment : Fragment(), RouteContract.View {

    private lateinit var recyclerView: RecyclerView
    private lateinit var kinButton: FloatingActionButton
    private lateinit var stationNameView: TextView
    private lateinit var swipeLayout: SwipeRefreshLayout
    private lateinit var searchPresenter: RoutePresenter
    private var selectedStation: BusStationData? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_route, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            adapter = BusRouteListAdapter()
                .apply {
                onItemClick = {
                    onBusRouteClick(it)
                }
            }
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) kinButton.hide() else kinButton.show()
                }
            })
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }

        kinButton = view.findViewById<FloatingActionButton>(R.id.fab).apply {
            setOnClickListener {
                selectedStation ?: return@setOnClickListener
                (activity as? MainActivity)?.handleSelectedBusStation(selectedStation!!, false)
            }
        }

        swipeLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeContainer).apply {
            setOnRefreshListener {
                selectedStation?.run {
                    searchPresenter.getRouteList(mobileNo)
                }
            }
        }

        stationNameView = view.findViewById(R.id.station_name)

        initPresenter()
    }

    private fun initPresenter() {
        searchPresenter = RoutePresenter()
        searchPresenter.takeView(this)
    }

    fun showRoute(station: BusStationData) {
        selectedStation = station
        searchPresenter.getRouteList(station.mobileNo)
        stationNameView.text = station.stationName + " [" + convertId(station.mobileNo) + "]"
    }

    private fun convertId(id: String): String = if ("0" == id) id else
        StringBuilder()
            .append(id.substring(0, 2))
            .append('-')
            .append(id.substring(2, id.length))
            .toString()

    private fun onBusRouteClick(data: RouteData) {
        Toast.makeText(requireContext(), data.busRouteId, Toast.LENGTH_SHORT).show()
    }

    override fun getLifecycleOwner(): LifecycleOwner {
        return viewLifecycleOwner
    }

    // RouteContract.View
    override fun showRouteList(stationList : List<RouteData>) {
        swipeLayout.isRefreshing = false
        kinButton.isEnabled = stationList.isNotEmpty()
        (recyclerView.adapter as? BusRouteListAdapter)?.setEntries(stationList)
    }

    // RouteContract.View
    override fun showError(error : String) {
        swipeLayout.isRefreshing = false
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}