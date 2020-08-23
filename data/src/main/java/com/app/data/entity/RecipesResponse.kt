package com.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.data.entity.RecipeEntity.Companion.TABLE_NAME
import com.app.domain.entity.RecipeData
import com.google.gson.annotations.SerializedName
import java.util.*

data class RecipesResponse(@SerializedName("offset")val offset: Int,
                           @SerializedName("number")val number: Int,
                           @SerializedName("results")val results: List<RecipeEntity>,
                           @SerializedName("totalResults")val totalResults: Int)

@Entity(tableName = TABLE_NAME)
data class RecipeEntity(@PrimaryKey(autoGenerate = false) @SerializedName("id")var recipeId: Long,
                        @SerializedName("title")val title: String,
                        @SerializedName("image") val imageLink: String,
                        val date: Date? = null){
    companion object {
        const val TABLE_NAME = "recipes"
    }
}

fun RecipeEntity.toBusinessEntity() = RecipeData(recipeId, title, imageLink, date)

fun RecipeData.toRawEntity() = RecipeEntity(recipeId, title, imageLink, date)