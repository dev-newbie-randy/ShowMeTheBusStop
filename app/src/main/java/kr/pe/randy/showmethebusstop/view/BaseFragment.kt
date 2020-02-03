package kr.pe.randy.showmethebusstop.view

import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    companion object {
        fun create(): BaseFragment {
            return BaseFragment()
        }
    }
}