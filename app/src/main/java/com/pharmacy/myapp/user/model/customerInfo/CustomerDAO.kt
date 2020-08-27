package com.pharmacy.myapp.user.model.customerInfo

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CustomerDAO {

    @Query("select * from customerinfo limit 1")
    fun get(): LiveData<CustomerInfo>

    @Query("select * from customerinfo limit 1")
    suspend fun getCustomer(): CustomerInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(customer: CustomerInfo)

    @Delete
    suspend fun delete(customer: CustomerInfo)

    @Update
    suspend fun update(customer: CustomerInfo)

    suspend fun isCustomerExist() = getCustomer() != null
}