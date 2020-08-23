package com.app.data.repository.datasource.recipe_summary

import com.app.data.entity.RecipeEntity
import com.app.data.entity.RecipeSummaryEntity

interface RecipesSummaryDataStore {

    suspend fun tryGetRecipeSummary(recipeId: Long): RecipeSummaryEntity

}