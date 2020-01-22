package kr.pe.randy.showmethebusstop.network

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "response", strict = false)
class StationSearchResponse {
    @field:Element(name = "msgBody")
    var msgBody: MsgBody? = null
}

@Root(name = "msgBody", strict = false)
class MsgBody {
    @field:ElementList(inline = true, name = "busStationList", required = false)
    var busStationList = mutableListOf<BusStation>()
}

@Root(name = "busStationList", strict = false)
data class BusStation(
    @field:Element(name = "centerYn", required = false)
    var centerYn: String = "",

    @field:Element(name = "districtCd", required = false)
    var districtCd: Int = 0,

    @field:Element(name = "mobileNo", required = false)
    var mobileNo: String = "",

    @field:Element(name = "regionName", required = false)
    var regionName: String = "",

    @field:Element(name = "stationId", required = false)
    var stationId: String = "",

    @field:Element(name = "stationName", required = false)
    var stationName: String = "",

    @field:Element(name = "x", required = false)
    var x: Double = 0.0,

    @field:Element(name = "y", required = false)
    var y: Double = 0.0
)