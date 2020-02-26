package kr.pe.randy.showmethebusstop.view.adapter

interface ItemActionListener {
    fun onItemMoved(from: Int, to: Int)
    fun onTouchUp()
}