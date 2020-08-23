package com.app.domain.use_cases.save_recipe

import com.app.domain.entity.RecipeAnalyzedInstructionsData
import com.app.domain.entity.RecipeData
import com.app.domain.entity.RecipeSummaryData

interface SaveRecipeUseCase {

    suspend fun saveRecipe(recipeData: RecipeData, recipeSummaryData: RecipeSummaryData, recipeAnalyzedInstructionsData: RecipeAnalyzedInstructionsData)

}