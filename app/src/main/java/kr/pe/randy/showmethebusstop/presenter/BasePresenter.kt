package kr.pe.randy.showmethebusstop.presenter

interface BasePresenter<T> {
    fun takeView(view: T)
    fun dropView()
}