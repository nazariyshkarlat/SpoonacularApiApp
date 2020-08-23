package com.app.data.database.recipe_analyzed_instructions

import androidx.room.TypeConverter
import com.app.data.entity.RecipeAnalyzedInstructionEntity
import com.app.data.entity.StepEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StepsTypeConverter {

    @TypeConverter
    fun listToJson(value: List<StepEntity>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson<List<StepEntity>>(value, object : TypeToken<List<StepEntity>>() {}.type).toList()
}