package com.pharmacy.data.local

import android.content.Context
import androidx.room.*
import com.pharmacy.categories.model.CategoryDAO
import com.pharmacy.core.general.interfaces.ManagerInterface
import com.pharmacy.data.remote.model.order.DeliveryInfoOrderData
import com.pharmacy.model.Picture
import com.pharmacy.model.category.Category
import com.pharmacy.model.product.RecentlyViewedDAO
import com.pharmacy.model.region.LocalRegion
import com.pharmacy.model.region.RegionDAO
import com.pharmacy.product.model.Product
import com.pharmacy.user.model.addressAndNote.AddressDAO
import com.pharmacy.user.model.customer.Customer
import com.pharmacy.user.model.customer.CustomerDAO

class DBManager(context: Context) : ManagerInterface {

    companion object {
        private const val NAME = "pharmacyDB"
        private const val VERSION = 4
    }

    private val db = Room
        .databaseBuilder(context.applicationContext, LocalDB::class.java, NAME)
        .apply { fallbackToDestructiveMigration() }
        .build()


    @Database(entities = [Customer::class, LocalRegion::class, Product::class, DeliveryInfoOrderData::class, Category::class], version = VERSION, exportSchema = false)
    @TypeConverters(StringListConverter::class, PicturesListConverter::class)
    abstract class LocalDB : RoomDatabase() {

        abstract fun customerDAO(): CustomerDAO

        abstract fun regionDAO(): RegionDAO

        abstract fun recentlyViewedDAO(): RecentlyViewedDAO

        abstract fun addressDAO(): AddressDAO

        abstract fun categoryDAO(): CategoryDAO

    }

    val customerDAO
        get() = db.customerDAO()

    val regionDAO
        get() = db.regionDAO()

    val recentlyViewedDAO
        get() = db.recentlyViewedDAO()

    val addressDAO
        get() = db.addressDAO()

    val categoryDAO
        get() = db.categoryDAO()

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