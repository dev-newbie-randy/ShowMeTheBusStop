package kr.pe.randy.showmethebusstop.model

data class RouteData(
    val staOrder: Int = 0,
    val routeId: String = "",
    val districtCd: Int = 0,
    val regionName: String = "",
    val routeTypeName: String = "",
    val routeTypeCd: String = "",
    val routeName: String = ""
)