package kr.pe.randy.showmethebusstop.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kr.pe.randy.showmethebusstop.MainActivity
import kr.pe.randy.showmethebusstop.R
import kr.pe.randy.showmethebusstop.adapter.BusStationListAdapter
import kr.pe.randy.showmethebusstop.data.BusStationData

class BusStationFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noResultView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_station, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            adapter = BusStationListAdapter().apply {
                onItemClick = {
                    onBusStationClick(it)
                }
            }
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }

        noResultView = view.findViewById<TextView>(R.id.no_result)
    }

    fun clearNoResult() {
        noResultView.visibility = View.GONE
    }

    fun bindList(list: List<BusStationData>) {
        if (list.isEmpty()) {
            noResultView.visibility = View.VISIBLE
        }
        (recyclerView.adapter as BusStationListAdapter).setEntries(list)
    }

    private fun onBusStationClick(data: BusStationData) {
        if (activity is MainActivity) {
            (activity as MainActivity).handleSelectedBusStation(data)
        }
    }
}