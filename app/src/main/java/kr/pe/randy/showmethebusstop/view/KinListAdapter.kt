package kr.pe.randy.showmethebusstop.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kr.pe.randy.showmethebusstop.R
import kr.pe.randy.showmethebusstop.model.BusStation

class KinListAdapter(private val onItemClick: (BusStation) -> Unit,
    private val onDeleteIconClick: (BusStation) -> Unit)
    : RecyclerView.Adapter<KinListAdapter.KinViewHolder>(){

    private val items = mutableListOf<BusStation>()
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

        fun bind(data: BusStation) {
            with(data) {
                busStationName.text = stationName
                busStationId.text = mobileNo
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

    fun setEntries(newList: List<BusStation>) {
        synchronized(items) {
            items.clear()
            items.addAll(newList)
            notifyDataSetChanged()
        }
    }
}