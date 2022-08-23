package co.kr.kwon.courierservice.data.db

import androidx.room.*
import co.kr.kwon.courierservice.data.entity.TrackingItem
import kotlinx.coroutines.flow.Flow


@Dao
interface TrackingItemDao {

    @Query("SELECT * FROM TrackingItem")
    fun allTrackingItems(): Flow<List<TrackingItem>>

    @Query("SELECT * FROM TrackingItem")
    fun getAll(): List<TrackingItem>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: TrackingItem)

    @Delete
    fun delete(item: TrackingItem)
}