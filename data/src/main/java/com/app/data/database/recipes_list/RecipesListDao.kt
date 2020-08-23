package com.app.data.database.recipes_list

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.data.entity.RecipeEntity

@Dao
interface RecipesListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipeEntity: RecipeEntity)

    @Query("DELETE FROM ${RecipeEntity.TABLE_NAME} WHERE recipeId = :recipeId")
    suspend fun deleteRecipe(recipeId: Long)

    @Query("SELECT * FROM ${RecipeEntity.TABLE_NAME} ORDER BY date DESC LIMIT :limit OFFSET :offset")
    suspend fun getRecipesList(offset: Int, limit: Int): List<RecipeEntity>

    @Query("SELECT EXISTS(SELECT * FROM ${RecipeEntity.TABLE_NAME} WHERE recipeId = :recipeId)")
    suspend fun checkRecipeSaved(recipeId: Long): Boolean
}