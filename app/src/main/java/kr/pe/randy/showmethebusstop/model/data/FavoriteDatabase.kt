package kr.pe.randy.showmethebusstop.model.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteStation::class], version = 1)
abstract class FavoriteDatabase: RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}