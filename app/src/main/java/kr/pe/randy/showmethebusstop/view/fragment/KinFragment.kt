package kr.pe.randy.showmethebusstop.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kr.pe.randy.showmethebusstop.MainActivity
import kr.pe.randy.showmethebusstop.R
import kr.pe.randy.showmethebusstop.data.entity.BusStationData
import kr.pe.randy.showmethebusstop.presenter.KinContract
import kr.pe.randy.showmethebusstop.presenter.KinPresenter
import kr.pe.randy.showmethebusstop.view.adapter.ItemTouchHelperCallback
import kr.pe.randy.showmethebusstop.view.adapter.KinListAdapter

class KinFragment : Fragment(), KinContract.View {
    private lateinit var fragmentView: View
    private val recyclerView by lazy {
        fragmentView.findViewById<RecyclerView>(R.id.recycler_view_favorite).apply {
            adapter = KinListAdapter(
                    onItemClick,
                    onDeleteIconClick,
                this@KinFragment
            )
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }
    }
    private val presenter = KinPresenter()
    private var addedStation: BusStationData? = null
    private var removeStation: BusStationData? = null
    private val itemTouchHelper by lazy {
        ItemTouchHelper(ItemTouchHelperCallback(recyclerView.adapter as KinListAdapter)).apply {
            attachToRecyclerView(recyclerView)
        }
    }

    private val onItemClick: (busStation: BusStationData) -> Unit = { item ->
        (requireActivity() as MainActivity).handleSelectedBusStation(item, false)
    }

    private val onDeleteIconClick: (busStation: BusStationData) -> Unit = { item ->
        (requireActivity() as MainActivity).handleSelectedBusStation(item, true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_kin, container, false).apply {
            fragmentView = this
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.takeView(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.loadFavorites()
    }

    fun addToFavorite(station: BusStationData, needNotify: Boolean) {
        addedStation = station
        presenter.addToFavorite(station, needNotify)
    }

    fun removeFromFavorite(station: BusStationData, needNotify: Boolean) {
        removeStation = station
        presenter.removeFromFavorite(station, needNotify)
    }

    // KinContract.View
    override fun showFavorites(stationList : List<BusStationData>) {
        (recyclerView.adapter as KinListAdapter).setEntries(stationList)
    }

    override fun getLifecycleOwner(): LifecycleOwner {
        return viewLifecycleOwner
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        Log.d("ssy79","onStartDrag()")
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun onEndDrag(items: List<Any>) {
        Log.e("ssy79","onEndDrag()")
        presenter.reorderFavorites(items as List<BusStationData>)
    }

    override fun showError(error : String) {
        Toast.makeText(requireContext(), requireContext().getText(R.string.favorite_already_exist), Toast.LENGTH_SHORT).show()
    }

    override fun showProgress() {
        fragmentView.findViewById<ContentLoadingProgressBar>(R.id.progress).visibility = View.VISIBLE
    }

    override fun hideProgress() {
        fragmentView.findViewById<ContentLoadingProgressBar>(R.id.progress).visibility = View.GONE
    }

    override fun notifyAdded() {
        Snackbar.make(recyclerView, requireContext().getText(R.string.favorite_added), Snackbar.LENGTH_SHORT).setAction(R.string.undo, ({
            addedStation?.let {
                removeFromFavorite(it, false)
            }
        })).show()
    }

    override fun notifyRemoved() {
        Snackbar.make(recyclerView, requireContext().getText(R.string.favorite_removed), Snackbar.LENGTH_SHORT).setAction(R.string.undo, ({
            removeStation?.let {
                addToFavorite(it, false)
            }
        })).show()
    }
    //
}