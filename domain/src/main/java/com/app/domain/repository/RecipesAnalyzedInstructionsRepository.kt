package com.app.domain.repository

import com.app.domain.entity.RecipeAnalyzedInstructionData
import com.app.domain.entity.RecipeAnalyzedInstructionsData

interface RecipesAnalyzedInstructionsRepository {

    suspend fun getRecipeAnalyzedInstructions(recipeId: Long, recipeIsSaved: Boolean) : RecipeAnalyzedInstructionsData

    suspend fun saveRecipeAnalyzedInstructions(recipeAnalyzedInstructionsData: RecipeAnalyzedInstructionsData)

    suspend fun deleteRecipeAnalyzedInstructions(recipeId: Long)

}