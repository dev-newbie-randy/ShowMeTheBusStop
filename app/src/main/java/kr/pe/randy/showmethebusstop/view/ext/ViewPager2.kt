package kr.pe.randy.showmethebusstop.view.ext

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

private fun ViewPager2.getCount(): Int {
    return adapter?.itemCount ?: 0
}

fun ViewPager2.moveNext() {
    if (currentItem + 1 >= getCount()) currentItem = 0 else currentItem++
}

fun ViewPager2.movePrev() {
    if (currentItem - 1 < 0) currentItem = getCount() - 1 else currentItem--
}

fun ViewPager2.reduceDragSensitivity() {
    val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
    recyclerViewField.isAccessible = true
    val recyclerView = recyclerViewField.get(this) as RecyclerView

    val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
    touchSlopField.isAccessible = true
    val touchSlop = touchSlopField.get(recyclerView) as Int
    touchSlopField.set(recyclerView, touchSlop * 3)       // "8" was obtained experimentally
}