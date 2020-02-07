package kr.pe.randy.showmethebusstop.model.data

import androidx.room.*

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg station: FavoriteStation)

    @Delete
    fun delete(vararg station: FavoriteStation)

    @Query("SELECT * FROM favorite")
    fun load(): List<FavoriteStation>
}