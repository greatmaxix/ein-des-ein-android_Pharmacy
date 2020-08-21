package com.pharmacy.myapp.model.customerInfo

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDAO {

    @Query("select * from customerinfo limit 1")
    fun get(): LiveData<CustomerInfo>

    @Query("select * from customerinfo limit 1")
    fun getCustomer(): CustomerInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(customer: CustomerInfo)

    @Delete
    fun delete(customer: CustomerInfo)

    @Update
    suspend fun update(customer: CustomerInfo)

}