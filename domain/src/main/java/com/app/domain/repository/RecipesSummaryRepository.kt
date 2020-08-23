package com.app.domain.repository

import com.app.domain.entity.RecipeSummaryData

interface RecipesSummaryRepository {

    suspend fun getRecipeSummary(recipeId: Long, recipeIsSaved: Boolean) : RecipeSummaryData

    suspend fun saveRecipeSummary(recipeSummaryData: RecipeSummaryData)

    suspend fun deleteRecipeSummary(recipeId: Long)
}