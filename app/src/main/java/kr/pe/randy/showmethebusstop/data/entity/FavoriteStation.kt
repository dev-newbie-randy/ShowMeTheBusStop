package kr.pe.randy.showmethebusstop.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
//autoGenerate = true
@Entity(tableName = "favorite")
data class FavoriteStation(
    @PrimaryKey(autoGenerate = false)
    val stationId: String,
    val stationNumber: String,
    val stationName: String
)
