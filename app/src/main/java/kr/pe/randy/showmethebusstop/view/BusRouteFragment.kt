package kr.pe.randy.showmethebusstop.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kr.pe.randy.showmethebusstop.R
import kr.pe.randy.showmethebusstop.model.RouteData
import kr.pe.randy.showmethebusstop.presenter.BusRouteListAdapter

class BusRouteFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

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
    }

    fun bindList(list: List<RouteData>) {
        (recyclerView.adapter as BusRouteListAdapter).setEntries(list)
    }

    private fun onBusRouteClick(data: RouteData) {
        Toast.makeText(activity, data.routeId, Toast.LENGTH_SHORT).show()
    }
}