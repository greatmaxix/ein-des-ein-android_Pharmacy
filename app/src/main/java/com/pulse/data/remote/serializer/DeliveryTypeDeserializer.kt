package com.pulse.data.remote.serializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.pulse.data.remote.model.order.DeliveryType
import java.lang.reflect.Type

class DeliveryTypeDeserializer : JsonDeserializer<DeliveryType> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?) =
        json?.asJsonPrimitive?.run { DeliveryType.getDeliveryType(asString) }

}