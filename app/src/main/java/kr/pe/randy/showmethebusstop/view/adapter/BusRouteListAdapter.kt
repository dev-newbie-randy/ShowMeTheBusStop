package kr.pe.randy.showmethebusstop.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import kr.pe.randy.showmethebusstop.R
import kr.pe.randy.showmethebusstop.data.entity.RouteData

class BusRouteListAdapter
    : RecyclerView.Adapter<BusRouteListAdapter.BusRouteViewHolder>() {

    private val items = mutableListOf<RouteData>()
    private lateinit var view: View
    var onItemClick: ((RouteData) -> Unit)? = null

    inner class BusRouteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val indicator = view.findViewById<Button>(R.id.left_wall)
        private val busRouteName = view.findViewById<TextView>(R.id.row_name)
        private val direction = view.findViewById<TextView>(R.id.row_direction)
        private val current = view.findViewById<TextView>(R.id.row_current)
        private val next = view.findViewById<TextView>(R.id.row_next)

        init {
            view.setOnClickListener {
                onItemClick?.invoke(items[adapterPosition])
            }
        }

        fun bind(data: RouteData) {
            @ColorInt val busColor = getBusColor(data.routeType)
            indicator.setBackgroundColor(busColor)
            busRouteName.text = data.rtNm
            busRouteName.setTextColor(busColor)
            direction.text = data.adirection
            direction.setTextColor(busColor)
            current.text = data.arrmsg1
            next.text = "다음버스 : " + data.arrmsg2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusRouteViewHolder {
        view = LayoutInflater.from(parent.context).inflate(R.layout.route_item_row, parent, false)
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

    //노선유형 (1:공항, 2:마을, 3:간선, 4:지선, 5:순환, 6:광역, 7:인천, 8:경기, 9:폐지, 0:공용)
    @ColorInt
    private fun getBusColor(routeType: String) = view.context.getColor(when (routeType) {
        "1" -> R.color.airport
        "2" -> R.color.village
        "3" -> R.color.gansun
        "4" -> R.color.jisun
        "5" -> R.color.circle
        "6" -> R.color.wide
        "7" -> R.color.incheon
        "8" -> R.color.gyunggi
        else -> android.R.color.black
    })
}