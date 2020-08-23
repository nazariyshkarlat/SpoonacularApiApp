package com.app.data.repository.datasource.recipe_analyzed_instructions

import com.app.data.entity.RecipeAnalyzedInstructionEntity
import com.app.data.entity.RecipeAnalyzedInstructionsEntity

interface RecipesAnalyzedInstructionsDataStore {

    suspend fun tryGetRecipeAnalyzedInstructions(recipeId: Long): RecipeAnalyzedInstructionsEntity

}