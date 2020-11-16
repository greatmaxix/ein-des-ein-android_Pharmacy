package com.pulse.data.remote.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.pulse.components.recipes.model.RecipeStatus
import java.lang.reflect.Type

class RecipesStatusDeserializer : JsonDeserializer<RecipeStatus> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?) =
        json?.asJsonPrimitive?.run { RecipeStatus.getRecipeStatus(asString) }

}