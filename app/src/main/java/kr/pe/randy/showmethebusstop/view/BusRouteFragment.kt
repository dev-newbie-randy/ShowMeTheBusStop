package kr.pe.randy.showmethebusstop.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kr.pe.randy.showmethebusstop.MainActivity
import kr.pe.randy.showmethebusstop.R
import kr.pe.randy.showmethebusstop.model.BusStation
import kr.pe.randy.showmethebusstop.model.RouteData
import kr.pe.randy.showmethebusstop.presenter.RouteContract
import kr.pe.randy.showmethebusstop.presenter.RoutePresenter

class BusRouteFragment : Fragment(), RouteContract.View {

    private lateinit var recyclerView: RecyclerView
    private lateinit var kinButton: FloatingActionButton
    private lateinit var stationNameView: TextView
    private lateinit var searchPresenter: RoutePresenter
    private var selectedStation: BusStation? = null

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
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }

        kinButton = view.findViewById<FloatingActionButton>(R.id.fab).apply {
            setOnClickListener {
                selectedStation ?: return@setOnClickListener
                (activity as? MainActivity)?.handleSelectedBusStation(selectedStation!!, true)
            }
        }

        stationNameView = view.findViewById(R.id.station_name)

        initPresenter()
    }

    private fun initPresenter() {
        searchPresenter = RoutePresenter()
        searchPresenter.takeView(this)
    }

    fun showRoute(station: BusStation) {
        selectedStation = station
        searchPresenter.getRouteList(station.stationId)
        stationNameView.text = station.stationName
    }

    private fun onBusRouteClick(data: RouteData) {
        Toast.makeText(requireContext(), data.routeId, Toast.LENGTH_SHORT).show()
    }

    override fun getContextIfAvailable(): Context? {
        return context
    }

    // RouteContract.View
    override fun showRouteList(stationList : List<RouteData>) {
        kinButton.isEnabled = stationList.isNotEmpty()
        (recyclerView.adapter as? BusRouteListAdapter)?.setEntries(stationList)
    }

    // RouteContract.View
    override fun showError(error : String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }
}