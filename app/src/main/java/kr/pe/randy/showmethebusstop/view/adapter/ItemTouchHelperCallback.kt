package kr.pe.randy.showmethebusstop.view.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ItemTouchHelperCallback(private val listener: ItemActionListener) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.DOWN or ItemTouchHelper.UP
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
    ): Boolean {
        listener.onItemMoved(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            (viewHolder as? KinListAdapter.KinViewHolder)?.apply {
                itemView.alpha = 0.5f
            }
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        (viewHolder as? KinListAdapter.KinViewHolder)?.apply {
            itemView.alpha = 1.0f
        }
        listener.onTouchUp()
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // TODO : swipe delete 구현 할 때 필요
    }

    override fun isLongPressDragEnabled(): Boolean = true

    override fun isItemViewSwipeEnabled(): Boolean = false
}