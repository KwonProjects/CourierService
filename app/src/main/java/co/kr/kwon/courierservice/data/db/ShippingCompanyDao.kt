package co.kr.kwon.courierservice.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.kr.kwon.courierservice.data.entity.ShippingCompany


@Dao
interface ShippingCompanyDao {

    @Query("SELECT * FROM ShippingCompany")
    fun getAll(): List<ShippingCompany>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(item: List<ShippingCompany>)
}