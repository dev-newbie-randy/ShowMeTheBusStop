package kr.pe.randy.showmethebusstop.ext

import androidx.viewpager2.widget.ViewPager2

private fun ViewPager2.getCount(): Int {
    return adapter?.itemCount ?: 0
}

fun ViewPager2.moveNext() {
    if (currentItem + 1 >= getCount()) currentItem = 0 else currentItem++
}

fun ViewPager2.movePrev() {
    if (currentItem - 1 <= 0) currentItem = getCount() - 1 else currentItem--
}
