package kr.pe.randy.showmethebusstop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.pe.randy.showmethebusstop.R
import kr.pe.randy.showmethebusstop.data.BusStationData

class BusStationListAdapter
    : RecyclerView.Adapter<BusStationListAdapter.BusStationViewHolder>() {

    private val items = mutableListOf<BusStationData>()
    var onItemClick: ((BusStationData) -> Unit)? = null

    inner class BusStationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val busStationName = view.findViewById<TextView>(R.id.row_name)
        private val busStationId = view.findViewById<TextView>(R.id.row_id)
        private val region = view.findViewById<TextView>(R.id.row_region)

        init {
            view.setOnClickListener {
                onItemClick?.invoke(items[adapterPosition])
            }
        }

        fun bind(data: BusStationData) {
            busStationName.text = data.stationName
            busStationId.text = data.mobileNo
            region.text = data.regionName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusStationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.station_item_row, parent, false)
        return BusStationViewHolder(view)
    }

    override fun onBindViewHolder(holder: BusStationViewHolder, pos: Int) =  holder.bind(items[pos])

    override fun getItemCount() = items.size

    fun setEntries(newList: List<BusStationData>) {
        synchronized(items) {
            items.clear()
            items.addAll(newList)
            notifyDataSetChanged()
        }
    }
}