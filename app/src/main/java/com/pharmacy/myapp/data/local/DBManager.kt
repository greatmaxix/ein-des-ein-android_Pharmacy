package com.pharmacy.myapp.data.local

import android.content.Context
import androidx.room.*
import com.pharmacy.myapp.core.general.interfaces.ManagerInterface
import com.pharmacy.myapp.model.Picture
import com.pharmacy.myapp.model.product.RecentlyViewedDAO
import com.pharmacy.myapp.model.region.RegionDAO
import com.pharmacy.myapp.model.region.LocalRegion
import com.pharmacy.myapp.product.model.Product
import com.pharmacy.myapp.user.model.customerInfo.CustomerDAO
import com.pharmacy.myapp.user.model.customerInfo.Customer

class DBManager(context: Context) : ManagerInterface {

    companion object {
        private const val NAME = "pharmacyDB"
        private const val VERSION = 4
    }

    private val db = Room
        .databaseBuilder(context.applicationContext, LocalDB::class.java, NAME)
        .apply { fallbackToDestructiveMigration() }
        .build()


    @Database(entities = [Customer::class, LocalRegion::class, Product::class], version = VERSION, exportSchema = false)
    @TypeConverters(StringListConverter::class, PicturesListConverter::class)
    abstract class LocalDB : RoomDatabase() {

        abstract fun customerDAO(): CustomerDAO

        abstract fun regionDAO(): RegionDAO

        abstract fun recentlyViewedDAO(): RecentlyViewedDAO

    }

    val customerDAO
        get() = db.customerDAO()

    val regionDAO
        get() = db.regionDAO()

    val recentlyViewedDAO
        get() = db.recentlyViewedDAO()

    class StringListConverter {
        @TypeConverter
        fun toList(value: String) = value.split("|").filter { it.isNotEmpty() }

        @TypeConverter
        fun fromList(list: List<String>) = list.joinToString("|")
    }

    class PicturesListConverter {
        @TypeConverter
        fun toList(value: String) = value.split("|").filter { it.isNotEmpty() }.map { Picture(it) }

        @TypeConverter
        fun fromList(list: List<Picture>) = list.joinToString("|") { it.url }
    }

    override fun clear() {
        db.clearAllTables()
    }
}