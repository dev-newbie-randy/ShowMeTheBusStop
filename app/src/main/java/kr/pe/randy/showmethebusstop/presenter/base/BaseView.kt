package kr.pe.randy.showmethebusstop.presenter.base

import androidx.lifecycle.LifecycleOwner

interface BaseView {
    fun getLifecycleOwner(): LifecycleOwner
    fun showError(error : String)
    fun showProgress()
    fun hideProgress()
}