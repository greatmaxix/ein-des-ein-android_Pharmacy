package com.pharmacy.myapp.user.model.addressAndNote

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.pharmacy.myapp.core.db.BaseDao
import com.pharmacy.myapp.data.remote.model.order.DeliveryInfoOrderData

@Dao
interface AddressDAO : BaseDao<DeliveryInfoOrderData> {

    @Query("select * from address limit 1")
    fun addressLiveData(): LiveData<DeliveryInfoOrderData>

    @Query("select * from address limit 1")
    fun get(): DeliveryInfoOrderData?

    @Query("DELETE FROM address")
    fun clear()
}