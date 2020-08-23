package com.app.data.database.recipe_summary

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.data.entity.RecipeEntity
import com.app.data.entity.RecipeSummaryEntity

@Dao
interface RecipesSummaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeSummary(recipeSummaryEntity: RecipeSummaryEntity)

    @Query("DELETE FROM ${RecipeSummaryEntity.TABLE_NAME} WHERE id = :recipeId")
    suspend fun deleteRecipeSummary(recipeId: Long)

    @Query("SELECT * FROM ${RecipeSummaryEntity.TABLE_NAME} WHERE id = :recipeId")
    suspend fun getRecipeSummary(recipeId: Long): RecipeSummaryEntity

}