package com.pharmacy.myapp.user.model.customer

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pharmacy.myapp.core.db.BaseDao

@Dao
interface CustomerDAO : BaseDao<Customer> {

    @Query("select * from customer limit 1")
    fun customerLiveData(): LiveData<Customer>

    @Query("select * from customer limit 1")
    suspend fun getCustomer(): Customer?

    suspend fun isCustomerExist() = getCustomer() != null
}