package com.pharmacy.myapp.data.local

import android.content.Context
import androidx.room.*
import com.pharmacy.myapp.model.TemporaryRegion
import com.pharmacy.myapp.user.model.customerInfo.CustomerDAO
import com.pharmacy.myapp.user.model.customerInfo.CustomerInfo
import com.pharmacy.myapp.model.region.RegionDAO

class DBManager(context: Context) {

    companion object {
        private const val NAME = "pharmacyDB"
        private const val VERSION = 3
    }

    private val db = Room
        .databaseBuilder(context.applicationContext, LocalDB::class.java, NAME)
        .apply { fallbackToDestructiveMigration() }
        .build()


    @Database(entities = [CustomerInfo::class, TemporaryRegion::class], version = VERSION, exportSchema = false)
    @TypeConverters(StringListConverter::class)
    abstract class LocalDB : RoomDatabase() {

        abstract fun customerDAO(): CustomerDAO

        abstract fun regionDAO(): RegionDAO

    }

    val customerDAO
        get() = db.customerDAO()

    fun regionDAO() = db.regionDAO()

    class StringListConverter {
        @TypeConverter
        fun toList(value: String) = value.split("|").filter { it.isNotEmpty() }

        @TypeConverter
        fun fromList(list: List<String>) = list.joinToString("|")
    }
}