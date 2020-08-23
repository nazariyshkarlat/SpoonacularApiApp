package com.app.data.database.recipe_analyzed_instructions

import androidx.room.TypeConverter
import com.app.data.entity.IngredientEntity
import com.app.data.entity.StepEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class IngredientsTypeConverter {

    @TypeConverter
    fun listToJson(value: List<IngredientEntity>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson<List<IngredientEntity>>(value, object : TypeToken<List<IngredientEntity>>() {}.type).toList()
}