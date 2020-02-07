package kr.pe.randy.showmethebusstop.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kr.pe.randy.showmethebusstop.MainActivity
import kr.pe.randy.showmethebusstop.R
import kr.pe.randy.showmethebusstop.model.BusStation
import kr.pe.randy.showmethebusstop.presenter.KinContract
import kr.pe.randy.showmethebusstop.presenter.KinPresenter

class KinFragment : Fragment(), KinContract.View {
    private lateinit var recyclerView: RecyclerView
    private lateinit var presenter: KinPresenter

    private val onItemClick: (busStation: BusStation) -> Unit = { item ->
        (requireActivity() as MainActivity).handleSelectedBusStation(item, false)
    }

    private val onDeleteIconClick: (busStation: BusStation) -> Unit = { item ->
        (requireActivity() as MainActivity).handleSelectedBusStation(item, true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return LayoutInflater.from(context).inflate(R.layout.fragment_kin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_favorite).apply {
            adapter = KinListAdapter(onItemClick, onDeleteIconClick)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }
        initPresenter()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.loadFavorites()
    }

    private fun initPresenter() {
        presenter = KinPresenter()
        presenter.takeView(this)
    }

    fun addToFavorite(station: BusStation) {
        presenter.addToFavorite(station)
    }

    fun removeFromFavorite(station: BusStation) {
        presenter.removeFromFavorite(station)
    }

    override fun getLifecycleOwner(): LifecycleOwner {
        return viewLifecycleOwner
    }

    // KinContract.View
    override fun showFavorites(stationList : List<BusStation>) {
        (recyclerView.adapter as KinListAdapter).setEntries(stationList)
    }

    // KinContract.View
    override fun showError(error : String) {
        Toast.makeText(requireContext(), requireContext().getText(R.string.favorite_already_exist), Toast.LENGTH_SHORT).show()
    }

    // KinContract.View
    override fun notifyAdded() {
        Snackbar.make(recyclerView, requireContext().getText(R.string.favorite_added), Snackbar.LENGTH_SHORT).show()
    }
}