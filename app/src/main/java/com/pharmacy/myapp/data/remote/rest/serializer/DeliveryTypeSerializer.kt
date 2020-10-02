package com.pharmacy.myapp.data.remote.rest.serializer

import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.pharmacy.myapp.orders.model.DeliveryType
import java.lang.reflect.Type

class DeliveryTypeSerializer : JsonSerializer<DeliveryType> {

    override fun serialize(src: DeliveryType?, typeOfSrc: Type?, context: JsonSerializationContext?) =
        JsonPrimitive(src?.type)
}