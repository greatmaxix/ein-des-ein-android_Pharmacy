package com.pharmacy.myapp.data.local

import android.content.Context
import androidx.room.*
import com.pharmacy.myapp.model.customerInfo.CustomerDAO
import com.pharmacy.myapp.model.customerInfo.CustomerInfo

class DBManager(context: Context) {

    companion object {
        private const val NAME = "pharmacyDB"
        private const val VERSION = 2
    }

    private val db = Room
        .databaseBuilder(context.applicationContext, LocalDB::class.java, NAME)
        .apply { fallbackToDestructiveMigration() }
        .build()

    @Database(entities = [CustomerInfo::class], version = VERSION, exportSchema = false)
    @TypeConverters(StringListConverter::class)
    abstract class LocalDB : RoomDatabase() {

        abstract fun customerDAO(): CustomerDAO

    }

    fun customerDAO() = db.customerDAO()

    class StringListConverter {
        @TypeConverter
        fun toList(value: String) = value.split("|").filter { it.isNotEmpty() }

        @TypeConverter
        fun fromList(list: List<String>) = list.joinToString("|")
    }
}