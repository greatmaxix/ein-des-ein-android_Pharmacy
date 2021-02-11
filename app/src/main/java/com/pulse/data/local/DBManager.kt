package com.pulse.data.local

import android.content.Context
import androidx.room.*
import com.pulse.components.categories.model.CategoryDAO
import com.pulse.components.chat.model.chat.ChatItem
import com.pulse.components.chat.model.chat.ChatItemDAO
import com.pulse.components.chat.model.message.MessageDAO
import com.pulse.components.chat.model.message.MessageItem
import com.pulse.components.chat.model.remoteKeys.RemoteKeys
import com.pulse.components.chat.model.remoteKeys.RemoteKeysDAO
import com.pulse.components.product.model.Product
import com.pulse.components.user.model.addressAndNote.AddressDAO
import com.pulse.components.user.model.customer.Customer
import com.pulse.components.user.model.customer.CustomerDAO
import com.pulse.core.general.interfaces.ManagerInterface
import com.pulse.data.remote.model.order.DeliveryInfoOrderData
import com.pulse.model.Picture
import com.pulse.model.category.Category
import com.pulse.model.product.RecentlyViewedDAO
import com.pulse.model.region.LocalRegion
import com.pulse.model.region.RegionDAO
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
            RemoteKeys::class,
            ChatItem::class
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

        abstract fun chatItemDAO(): ChatItemDAO
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

    val chatItemDAO
        get() = db.chatItemDAO()

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