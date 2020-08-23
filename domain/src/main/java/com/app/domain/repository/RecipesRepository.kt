package com.app.domain.repository

import com.app.domain.entity.RecipeData
import com.app.domain.use_cases.get_recipes.GetRecipesUseCase

interface RecipesRepository {

    suspend fun getRecipes(query: String?, perPage: Int, pageNumber: Int, sort: GetRecipesUseCase.Sort, areSavedRecipes: Boolean) : List<RecipeData>

    suspend fun saveRecipe(recipeData: RecipeData)

    suspend fun deleteRecipe(recipeId: Long)

    suspend fun isRecipeSaved(recipeId: Long): Boolean
}