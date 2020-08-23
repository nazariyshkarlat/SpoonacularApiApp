package com.app.domain.use_cases.get_recipe_analyzed_instructions

import com.app.domain.use_cases.GetRecipeAnalyzedInstructionsStatus

interface GetRecipeAnalyzedInstructionsUseCase {

    val retryDelay: Long
        get() = 1000L

    val retryCount: Int
        get() = 3

    suspend fun getRecipeAnalyzedInstructions(recipeId: Long, retryCount: Int = this.retryCount, recipeIsSaved: Boolean): GetRecipeAnalyzedInstructionsStatus
}