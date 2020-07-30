package com.pharmacy.myapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pharmacy.myapp.model.customerInfo.CustomerDAO
import com.pharmacy.myapp.model.customerInfo.CustomerInfo

class DBManager(context: Context) {

    companion object {
        private const val VERSION = 1
    }

    private val db: InMemoryDB

    init {
        db = Room.inMemoryDatabaseBuilder(context.applicationContext, InMemoryDB::class.java).apply {
            fallbackToDestructiveMigration()
        }.build()
    }

    @Database(
        entities = [CustomerInfo::class],
        version = VERSION,
        exportSchema = false
    )
    abstract class InMemoryDB : RoomDatabase() {

        abstract fun customerDAO(): CustomerDAO
    }

    fun getCustomerDAO() = db.customerDAO()

}