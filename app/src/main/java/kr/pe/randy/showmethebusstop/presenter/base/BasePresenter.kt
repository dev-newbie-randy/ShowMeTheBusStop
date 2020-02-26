package kr.pe.randy.showmethebusstop.presenter.base

interface BasePresenter<T> {
    fun takeView(view: T)
    fun dropView()
}