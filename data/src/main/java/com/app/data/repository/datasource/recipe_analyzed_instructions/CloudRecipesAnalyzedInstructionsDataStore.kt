package com.app.data.repository.datasource.recipe_analyzed_instructions

import com.app.data.entity.RecipeAnalyzedInstructionsEntity
import com.app.data.net.RecipeAnalyzedInstructionsService
import com.app.data.net.connection.ConnectionManager
import com.app.domain.exception.NetworkConnectionException
import com.app.domain.exception.ServerUnavailableException
import kotlinx.coroutines.CancellationException

class CloudRecipesAnalyzedInstructionsDataStore(
    private val connectionManager: ConnectionManager,
    private val recipeAnalyzedInstructionsService: RecipeAnalyzedInstructionsService) :
    RecipesAnalyzedInstructionsDataStore {

    override suspend fun tryGetRecipeAnalyzedInstructions(recipeId: Long): RecipeAnalyzedInstructionsEntity =
        if (connectionManager.isNetworkAvailable()) {
            try {
                RecipeAnalyzedInstructionsEntity(recipeId, recipeAnalyzedInstructionsService.getRecipeAnalyzedInstructions(recipeId).body()!!)
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