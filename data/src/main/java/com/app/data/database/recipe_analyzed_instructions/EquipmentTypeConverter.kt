package com.app.data.database.recipe_analyzed_instructions

import androidx.room.TypeConverter
import com.app.data.entity.EquipmentEntity
import com.app.data.entity.IngredientEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class EquipmentTypeConverter {

    @TypeConverter
    fun listToJson(value: List<EquipmentEntity>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson<List<EquipmentEntity>>(value, object : TypeToken<List<EquipmentEntity>>() {}.type).toList()
}