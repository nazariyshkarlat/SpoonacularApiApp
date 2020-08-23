package com.app.data.repository.datasource.recipe_summary

import com.app.data.entity.RecipeSummaryEntity
import com.app.data.net.RecipeSummaryService
import com.app.data.net.connection.ConnectionManager
import com.app.domain.exception.NetworkConnectionException
import com.app.domain.exception.ServerUnavailableException
import kotlinx.coroutines.CancellationException

class CloudRecipesSummaryDataStore(
    private val connectionManager: ConnectionManager,
    private val recipeSummaryService: RecipeSummaryService) :
    RecipesSummaryDataStore {

    override suspend fun tryGetRecipeSummary(recipeId: Long): RecipeSummaryEntity =
        if (connectionManager.isNetworkAvailable()) {
            try {
                recipeSummaryService.getRecipeSummary(recipeId).body()!!
            } catch (exception: Exception) {
                if (exception is CancellationException)
                    throw exception
                else
                    throw ServerUnavailableException()
            }
        } else {
            throw NetworkConnectionException()
        }

}