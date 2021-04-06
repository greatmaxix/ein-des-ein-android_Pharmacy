package com.pulse.components.user.model.customer

import androidx.room.Dao
import androidx.room.Query
import com.pulse.core.db.BaseDao
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDAO : BaseDao<Customer> {

    @Query("select * from customer limit 1")
    fun customerFlow(): Flow<Customer?>

    @Query("select * from customer limit 1")
    suspend fun getCustomer(): Customer?

    @Deprecated("We have to remove this method because it's a part of business logic @Customer != null@, repository provides only data from Data Sources")
    suspend fun isCustomerExist() = getCustomer() != null
}