package kr.pe.randy.showmethebusstop.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.pe.randy.showmethebusstop.R
import kr.pe.randy.showmethebusstop.model.RouteData

class BusRouteListAdapter
    : RecyclerView.Adapter<BusRouteListAdapter.BusRouteViewHolder>() {

    private val items = mutableListOf<RouteData>()
    var onItemClick: ((RouteData) -> Unit)? = null

    inner class BusRouteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val busRouteName = view.findViewById<TextView>(R.id.row_name)
        private val busType = view.findViewById<TextView>(R.id.row_type)

        init {
            view.setOnClickListener {
                onItemClick?.invoke(items[adapterPosition])
            }
        }

        fun bind(data: RouteData) {
            busRouteName.text = data.routeName + data.regionName
            busType.text = data.routeTypeName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusRouteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.route_item_row, parent, false)
        return BusRouteViewHolder(view)
    }

    override fun onBindViewHolder(holder: BusRouteViewHolder, pos: Int) =  holder.bind(items[pos])

    override fun getItemCount() = items.size

    fun setEntries(newList: List<RouteData>) {
        synchronized(items) {
            items.clear()
            items.addAll(newList)
            notifyDataSetChanged()
        }
    }
}