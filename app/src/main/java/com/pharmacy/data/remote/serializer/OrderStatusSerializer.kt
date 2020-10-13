package com.pharmacy.data.remote.serializer

import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.pharmacy.model.order.OrderStatus
import java.lang.reflect.Type

class OrderStatusSerializer : JsonSerializer<OrderStatus> {

    override fun serialize(src: OrderStatus?, typeOfSrc: Type?, context: JsonSerializationContext?) =
        JsonPrimitive(src?.status)
}