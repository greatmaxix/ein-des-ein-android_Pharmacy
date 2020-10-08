package com.pharmacy.myapp.data.remote.serializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.pharmacy.myapp.model.order.OrderStatus
import java.lang.reflect.Type

class OrderStatusDeserializer : JsonDeserializer<OrderStatus> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?) =
        json?.asJsonPrimitive?.run { OrderStatus.getOrderStatus(asString) }

}