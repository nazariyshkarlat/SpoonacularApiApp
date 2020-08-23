package com.app.domain.use_cases.check_is_saved

interface CheckRecipeIsSavedUseCase {

    suspend fun recipeIsSaved(recipeId: Long): Boolean

}