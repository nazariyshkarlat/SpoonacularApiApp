package com.app.data.repository.datasource.recipe_summary

import com.app.data.database.recipe_summary.RecipesSummaryDao
import com.app.data.entity.RecipeSummaryEntity
import com.app.domain.exception.NetworkConnectionException
import kotlinx.coroutines.CancellationException

class CacheRecipesSummaryDataStore(
    private val recipesSummaryDao: RecipesSummaryDao
) : RecipesSummaryDataStore {

    override suspend fun tryGetRecipeSummary(recipeId: Long): RecipeSummaryEntity =
        try {
            recipesSummaryDao.getRecipeSummary(recipeId)
        } catch (exception: Exception) {
            if (exception is CancellationException)
                throw exception
            else
                throw NetworkConnectionException()
        }

    suspend fun trySaveRecipeSummary(recipeSummaryEntity: RecipeSummaryEntity){
        recipesSummaryDao.insertRecipeSummary(recipeSummaryEntity)
    }

    suspend fun tryDeleteRecipeSummary(recipeId: Long){
        recipesSummaryDao.deleteRecipeSummary(recipeId)
    }
}