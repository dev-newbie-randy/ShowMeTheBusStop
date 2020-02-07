package kr.pe.randy.showmethebusstop.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "ServiceResult", strict = false)
class StationSearchResponse {
    @field:Element(name = "msgHeader")
    var msgHeader: MsgHeader? = null

    @field:Element(name = "msgBody")
    var msgBody: MsgBody? = null
}

@Root(name = "msgHeader", strict = false)
class MsgHeader {

    @field:Element(name = "headerCd")
    var headerCd: String = ""

    @field:Element(name = "headerMsg")
    var headerMsg: String = ""

    @field:Element(name = "itemCount")
    var itemCount: Int = 0
}

@Root(name = "msgBody", strict = false)
class MsgBody {
    @field:ElementList(inline = true, name = "itemList", required = false)
    var busStationList = mutableListOf<BusStation>()
}

@Root(name = "itemList", strict = false)
data class BusStation(
    @field:Element(name = "arsId" , required = false)
    var mobileNo: String = "",

    @field:Element(name = "posX", required = false)
    var posX: String = "",

    @field:Element(name = "posY", required = false)
    var posY: String = "",

    @field:Element(name = "stId", required = false)
    var stationId: String = "",

    @field:Element(name = "stNm", required = false)
    var stationName: String = "",

    @field:Element(name = "tmX", required = false)
    var tmX: String = "",

    @field:Element(name = "tmY", required = false)
    var tmY: String = ""
)