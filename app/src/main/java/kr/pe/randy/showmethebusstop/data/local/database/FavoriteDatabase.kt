package kr.pe.randy.showmethebusstop.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import kr.pe.randy.showmethebusstop.data.entity.FavoriteStation

@Database(entities = [FavoriteStation::class], version = 1)
abstract class FavoriteDatabase: RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}