package com.app.data.database.recipe_analyzed_instructions

import androidx.room.TypeConverter
import com.app.data.entity.RecipeAnalyzedInstructionEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipeAnalyzedInstructionsTypeConverter {

    @TypeConverter
    fun listToJson(value: List<RecipeAnalyzedInstructionEntity>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson<List<RecipeAnalyzedInstructionEntity>>(value, object : TypeToken<List<RecipeAnalyzedInstructionEntity>>() {}.type).toList()
}