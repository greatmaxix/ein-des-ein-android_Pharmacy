package com.pulse.data.remote.deserializer

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.pulse.categories.extra.CategoryWrapper
import com.pulse.model.category.Category
import timber.log.Timber
import java.lang.reflect.Type

class CategoryDeserializer : JsonDeserializer<Category> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?) =
        json?.asJsonObject?.run { Gson().fromJson(json, Category::class.java) }?.apply {
            CategoryWrapper.categoryImage[name]?.let {
                Timber.e(name)
                drawableName = it
            }
        }
}