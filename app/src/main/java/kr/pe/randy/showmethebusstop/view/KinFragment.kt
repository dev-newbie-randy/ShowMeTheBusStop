package kr.pe.randy.showmethebusstop.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kr.pe.randy.showmethebusstop.MainActivity
import kr.pe.randy.showmethebusstop.R
import kr.pe.randy.showmethebusstop.model.BusStation

class KinFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

    private val onItemClick: (busStation: BusStation) -> Unit = { item ->
        (requireActivity() as MainActivity).handleSelectedBusStation(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_kin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            adapter = KinListAdapter(onItemClick)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }
    }

    fun addToFavorite(station: BusStation) {
        (recyclerView.adapter as KinListAdapter).addToFavorite(station)
    }
}