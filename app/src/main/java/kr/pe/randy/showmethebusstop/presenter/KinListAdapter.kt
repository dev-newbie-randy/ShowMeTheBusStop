package kr.pe.randy.showmethebusstop.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.pe.randy.showmethebusstop.R
import kr.pe.randy.showmethebusstop.model.BusStation

class KinListAdapter
    : RecyclerView.Adapter<KinListAdapter.KinViewHolder>(){

    private val items = mutableListOf<BusStation>()

    inner class KinViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val busStationName = view.findViewById<TextView>(R.id.row_name)
        private val busStationId = view.findViewById<TextView>(R.id.row_id)
        private val region = view.findViewById<TextView>(R.id.row_region)

        fun bind(data: BusStation) {
            with(data) {
                busStationName.text = stationName
                busStationId.text = mobileNo
                region.text = regionName
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KinViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.station_item_row, parent, false)
        return KinViewHolder(view)
    }

    override fun onBindViewHolder(holder: KinViewHolder, pos: Int) =  holder.bind(items[pos])

    override fun getItemCount() = items.size

    fun addToFavorite(station: BusStation) {
        synchronized(items) {
            items.asSequence().find {
                it == station
            } ?: run {
                items.add(station)
            }
            notifyDataSetChanged()
        }
    }
}