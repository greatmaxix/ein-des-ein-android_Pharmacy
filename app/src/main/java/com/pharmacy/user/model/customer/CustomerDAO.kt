package com.pharmacy.user.model.customer

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.pharmacy.core.db.BaseDao

@Dao
interface CustomerDAO : BaseDao<Customer> {

    @Query("select * from customer limit 1")
    fun customerLiveData(): LiveData<Customer?>

    @Query("select * from customer limit 1")
    suspend fun getCustomer(): Customer?

    suspend fun isCustomerExist() = getCustomer() != null
}