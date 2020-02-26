package kr.pe.randy.showmethebusstop.data.local.database

import androidx.room.*
import kr.pe.randy.showmethebusstop.data.entity.FavoriteStation

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg station: FavoriteStation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stations: List<FavoriteStation>)

    @Delete
    suspend fun delete(vararg station: FavoriteStation)

    @Query("DELETE FROM favorite")
    suspend fun deleteAll()

    @Query("SELECT * FROM favorite")
    suspend fun load(): List<FavoriteStation>
}