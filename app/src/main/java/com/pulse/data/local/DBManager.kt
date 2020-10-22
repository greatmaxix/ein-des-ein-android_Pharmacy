package com.pulse.data.local

import android.content.Context
import androidx.room.*
import com.pulse.categories.model.CategoryDAO
import com.pulse.core.general.interfaces.ManagerInterface
import com.pulse.data.remote.model.order.DeliveryInfoOrderData
import com.pulse.model.Picture
import com.pulse.model.category.Category
import com.pulse.model.product.RecentlyViewedDAO
import com.pulse.model.region.LocalRegion
import com.pulse.model.region.RegionDAO
import com.pulse.product.model.Product
import com.pulse.user.model.addressAndNote.AddressDAO
import com.pulse.user.model.customer.Customer
import com.pulse.user.model.customer.CustomerDAO

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