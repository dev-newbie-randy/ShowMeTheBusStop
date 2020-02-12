package kr.pe.randy.showmethebusstop.data.local.database

import androidx.room.*
import kr.pe.randy.showmethebusstop.data.entity.FavoriteStation

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg station: FavoriteStation)

    @Delete
    suspend fun delete(vararg station: FavoriteStation)

    @Query("SELECT * FROM favorite")
    suspend fun load(): List<FavoriteStation>
}