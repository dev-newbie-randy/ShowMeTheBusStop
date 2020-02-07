package kr.pe.randy.showmethebusstop.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey
//autoGenerate = true
@Entity(tableName = "favorite")
data class FavoriteStation(
    @PrimaryKey
    val stationId: String,
    val stationNumber: String,
    val stationName: String,
    val regionName: String
)
