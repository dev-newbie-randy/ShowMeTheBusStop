package kr.pe.randy.showmethebusstop.view

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kr.pe.randy.showmethebusstop.R
import kr.pe.randy.showmethebusstop.model.BusStation

class KinListAdapter
    : RecyclerView.Adapter<KinListAdapter.KinViewHolder>(){

    private val items = mutableListOf<BusStation>()
    private lateinit var rootView: View

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
        rootView = LayoutInflater.from(parent.context).inflate(R.layout.station_item_row, parent, false)
        return KinViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: KinViewHolder, pos: Int) =  holder.bind(items[pos])

    override fun getItemCount() = items.size

    fun addToFavorite(station: BusStation) {
        synchronized(items) {
            items.asSequence().find {
                it == station
            }?.also {
                rootView.post {
                    Toast.makeText(rootView.context, "풉 이미 즐겨찾기 됐는데~", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                items.add(station)
                Handler().postDelayed( {
                    Snackbar.make(rootView, "즐겨찾기에 추가되었습니다.", Snackbar.LENGTH_SHORT).show()
                }, 500)
            }
            notifyDataSetChanged()
        }
    }
}