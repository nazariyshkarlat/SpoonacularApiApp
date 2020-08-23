package com.app.domain.use_cases.check_is_saved

import com.app.domain.repository.RecipesRepository

class CheckRecipeIsSavedUserCaseImpl(val repository: RecipesRepository) : CheckRecipeIsSavedUseCase {
    override suspend fun recipeIsSaved(recipeId: Long): Boolean = repository.isRecipeSaved(recipeId)
}