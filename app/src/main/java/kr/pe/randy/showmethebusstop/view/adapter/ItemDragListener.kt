package kr.pe.randy.showmethebusstop.view.adapter

import androidx.recyclerview.widget.RecyclerView

interface ItemDragListener {
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
    fun onEndDrag(items: List<Any>)
}