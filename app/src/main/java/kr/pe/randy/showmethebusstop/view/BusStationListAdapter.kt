package kr.pe.randy.showmethebusstop.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.pe.randy.showmethebusstop.R
import kr.pe.randy.showmethebusstop.model.BusStation

class BusStationListAdapter
    : RecyclerView.Adapter<BusStationListAdapter.BusStationViewHolder>() {

    private val items = mutableListOf<BusStation>()
    var onItemClick: ((BusStation) -> Unit)? = null

    inner class BusStationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val busStationName = view.findViewById<TextView>(R.id.row_name)
        private val busStationId = view.findViewById<TextView>(R.id.row_id)

        init {
            view.setOnClickListener {
                onItemClick?.invoke(items[adapterPosition])
            }
        }

        fun bind(data: BusStation) {
            with(data) {
                busStationName.text = stationName
                busStationId.text = mobileNo
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusStationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.station_item_row, parent, false)
        return BusStationViewHolder(view)
    }

    override fun onBindViewHolder(holder: BusStationViewHolder, pos: Int) =  holder.bind(items[pos])

    override fun getItemCount() = items.size

    fun setEntries(newList: List<BusStation>) {
        synchronized(items) {
            items.clear()
            items.addAll(newList)
            notifyDataSetChanged()
        }
    }
}