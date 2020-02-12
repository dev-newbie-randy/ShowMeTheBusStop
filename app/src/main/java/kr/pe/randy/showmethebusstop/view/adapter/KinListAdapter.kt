package kr.pe.randy.showmethebusstop.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kr.pe.randy.showmethebusstop.R
import kr.pe.randy.showmethebusstop.data.entity.BusStationData

class KinListAdapter(private val onItemClick: (BusStationData) -> Unit,
                     private val onDeleteIconClick: (BusStationData) -> Unit)
    : RecyclerView.Adapter<KinListAdapter.KinViewHolder>(){

    private val items = mutableListOf<BusStationData>()
    private lateinit var rootView: View

    inner class KinViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val busStationName = view.findViewById<TextView>(R.id.row_name)
        private val busStationId = view.findViewById<TextView>(R.id.row_id)
        private val delete = view.findViewById<AppCompatImageButton>(R.id.delete_button)

        init {
            rootView.setOnClickListener {
                onItemClick(items[adapterPosition])
            }

            rootView.findViewById<AppCompatImageButton>(R.id.delete_button).setOnClickListener {
                onDeleteIconClick(items[adapterPosition])
            }
        }

        fun bind(data: BusStationData) {
            with(data) {
                busStationName.text = stationName
                busStationId.text = convertId(mobileNo)
                delete.isVisible = true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KinViewHolder {
        rootView = LayoutInflater.from(parent.context).inflate(R.layout.kin_item_row, parent, false)
        return KinViewHolder(rootView)
    }

    override fun onBindViewHolder(holder: KinViewHolder, pos: Int) =  holder.bind(items[pos])

    override fun getItemCount() = items.size

    fun setEntries(newList: List<BusStationData>) {
        synchronized(items) {
            items.clear()
            items.addAll(newList)
            notifyDataSetChanged()
        }
    }

    private fun convertId(id: String): String = if ("0" == id) id else
        StringBuilder()
            .append(id.substring(0, 2))
            .append('-')
            .append(id.substring(2, id.length))
            .toString()
}