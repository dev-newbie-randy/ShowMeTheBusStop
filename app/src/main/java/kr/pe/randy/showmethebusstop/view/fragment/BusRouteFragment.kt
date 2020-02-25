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
    private lateinit var fragmentView: View

    private val recyclerView by lazy {
        fragmentView.findViewById<RecyclerView>(R.id.recycler_view).apply {
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
    }

    private val kinButton by lazy {
        fragmentView.findViewById<FloatingActionButton>(R.id.fab).apply {
            setOnClickListener {
                selectedStation ?: return@setOnClickListener
                (activity as? MainActivity)?.handleSelectedBusStation(selectedStation!!, false)
            }
        }
    }

    private val swipeLayout by lazy {
        fragmentView.findViewById<SwipeRefreshLayout>(R.id.swipeContainer).apply {
            setOnRefreshListener {
                selectedStation?.run {
                    presenter.getRouteList(mobileNo)
                }
            }
        }
    }

    private val stationNameView by lazy {
        fragmentView.findViewById<TextView>(R.id.station_name)
    }

    private val presenter = RoutePresenter()
    private var selectedStation: BusStationData? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_route, container, false).apply {
            fragmentView = this
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.takeView(this)
    }

    fun showRoute(station: BusStationData) {
        selectedStation = station
        presenter.getRouteList(station.mobileNo)
        stationNameView.text = station.stationName + " [" + convertId(station.mobileNo) + "]"
    }

    private fun convertId(id: String): String = if ("0" == id) id else
        StringBuilder()
            .append(id.substring(0, 2))
            .append('-')
            .append(id.substring(2, id.length))
            .toString()

    // RouteContract.View
    override fun showRouteList(stationList : List<RouteData>) {
        swipeLayout.isRefreshing = false
        kinButton.isEnabled = stationList.isNotEmpty()
        (recyclerView.adapter as? BusRouteListAdapter)?.setEntries(stationList)
    }

    override fun getLifecycleOwner(): LifecycleOwner {
        return viewLifecycleOwner
    }

    override fun showError(error : String) {
        swipeLayout.isRefreshing = false
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun showProgress() {
        fragmentView.findViewById<ContentLoadingProgressBar>(R.id.progress).visibility = View.VISIBLE
    }

    override fun hideProgress() {
        fragmentView.findViewById<ContentLoadingProgressBar>(R.id.progress).visibility = View.GONE
    }
    //

    private fun onBusRouteClick(data: RouteData) {
        Toast.makeText(requireContext(), data.busRouteId, Toast.LENGTH_SHORT).show()
    }
}