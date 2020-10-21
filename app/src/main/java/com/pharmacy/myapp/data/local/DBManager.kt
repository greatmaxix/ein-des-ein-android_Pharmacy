package com.pharmacy.myapp.data.local

import android.content.Context
import androidx.room.*
import com.pharmacy.myapp.categories.model.CategoryDAO
import com.pharmacy.myapp.chat.model.message.MessageDAO
import com.pharmacy.myapp.chat.model.message.MessageItem
import com.pharmacy.myapp.chat.model.remoteKeys.RemoteKeys
import com.pharmacy.myapp.chat.model.remoteKeys.RemoteKeysDAO
import com.pharmacy.myapp.core.general.interfaces.ManagerInterface
import com.pharmacy.myapp.data.remote.model.order.DeliveryInfoOrderData
import com.pharmacy.myapp.model.Picture
import com.pharmacy.myapp.model.category.Category
import com.pharmacy.myapp.model.product.RecentlyViewedDAO
import com.pharmacy.myapp.model.region.LocalRegion
import com.pharmacy.myapp.model.region.RegionDAO
import com.pharmacy.myapp.product.model.Product
import com.pharmacy.myapp.user.model.addressAndNote.AddressDAO
import com.pharmacy.myapp.user.model.customer.Customer
import com.pharmacy.myapp.user.model.customer.CustomerDAO
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class DBManager(context: Context) : ManagerInterface {

    companion object {
        private const val NAME = "pharmacyDB"
        private const val VERSION = 5
    }

    private val db = Room
        .databaseBuilder(context.applicationContext, LocalDB::class.java, NAME)
        .apply { fallbackToDestructiveMigration() }
        .build()

    override fun clear() {
        db.clearAllTables()
    }

    @Database(
        entities = [
            Customer::class,
            LocalRegion::class,
            Product::class,
            DeliveryInfoOrderData::class,
            Category::class,
            MessageItem::class,
            RemoteKeys::class
        ],
        version = VERSION,
        exportSchema = false
    )
    @TypeConverters(
        StringListConverter::class,
        PicturesListConverter::class,
        LocalDateTimeConverter::class
    )
    abstract class LocalDB : RoomDatabase() {

        abstract fun customerDAO(): CustomerDAO

        abstract fun regionDAO(): RegionDAO

        abstract fun recentlyViewedDAO(): RecentlyViewedDAO

        abstract fun addressDAO(): AddressDAO

        abstract fun categoryDAO(): CategoryDAO

        abstract fun messageDAO(): MessageDAO

        abstract fun remoteKeysDAO(): RemoteKeysDAO
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

    val messageDAO
        get() = db.messageDAO()

    val remoteKeysDAO
        get() = db.remoteKeysDAO()

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

    class LocalDateTimeConverter {
        @TypeConverter
        fun toLocalDateTime(value: Long?) = value?.let { LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneOffset.UTC) }

        @TypeConverter
        fun fromLocalDateTime(localDateTime: LocalDateTime) = localDateTime.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()
    }
}