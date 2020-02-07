package kr.pe.randy.showmethebusstop

import android.app.Application
import androidx.room.Room
import kr.pe.randy.showmethebusstop.model.data.FavoriteDatabase

class App : Application() {
    companion object {
        @Volatile
        private lateinit var database: FavoriteDatabase

        val DATABASE: FavoriteDatabase
            get() = database
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
                this,
                FavoriteDatabase::class.java,
                "favorite-station-db"
        ).build()
    }
}