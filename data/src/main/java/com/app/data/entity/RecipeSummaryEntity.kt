package com.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.domain.entity.RecipeData
import com.app.domain.entity.RecipeSummaryData
import com.google.gson.annotations.SerializedName

@Entity(tableName = RecipeSummaryEntity.TABLE_NAME)
data class RecipeSummaryEntity(@PrimaryKey(autoGenerate = false) @SerializedName("id")var id: Long, @SerializedName("title")val title: String, @SerializedName("summary")val summary: String){
    companion object {
        const val TABLE_NAME = "recipes_summary"
    }
}

fun RecipeSummaryEntity.toBusinessEntity() = RecipeSummaryData(id, title, summary)

fun RecipeSummaryData.toRawEntity() = RecipeSummaryEntity(recipeId, title, summary)