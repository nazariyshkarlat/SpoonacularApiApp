package com.app.domain.use_cases.delete_recipe

interface DeleteRecipeUseCase {

    suspend fun deleteRecipe(recipeId: Long)

}