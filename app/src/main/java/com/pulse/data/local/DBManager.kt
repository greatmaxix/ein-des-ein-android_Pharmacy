package com.pulse.data.local

import android.content.Context
import androidx.room.*
import com.pulse.categories.model.CategoryDAO
import com.pulse.chat.model.message.MessageDAO
import com.pulse.chat.model.message.MessageItem
import com.pulse.chat.model.remoteKeys.RemoteKeys
import com.pulse.chat.model.remoteKeys.RemoteKeysDAO
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