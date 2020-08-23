package com.app.data.database.recipe_analyzed_instructions

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.data.entity.RecipeAnalyzedInstructionsEntity

@Dao
interface RecipesAnalyzedInstructionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipeAnalyzedInstructions(recipeAnalyzedInstructions: RecipeAnalyzedInstructionsEntity)

    @Query("DELETE FROM ${RecipeAnalyzedInstructionsEntity.TABLE_NAME} WHERE id = :recipeId")
    suspend fun deleteRecipeAnalyzedInstructions(recipeId: Long)

    @Query("SELECT * FROM ${RecipeAnalyzedInstructionsEntity.TABLE_NAME} WHERE id = :recipeId")
    suspend fun getRecipeAnalyzedInstructions(recipeId: Long): RecipeAnalyzedInstructionsEntity

}