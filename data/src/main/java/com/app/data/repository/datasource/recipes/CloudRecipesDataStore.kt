package com.app.data.repository.datasource.recipes

import com.app.data.entity.RecipeEntity
import com.app.data.net.RecipesService
import com.app.data.net.connection.ConnectionManager
import com.app.domain.exception.NetworkConnectionException
import com.app.domain.exception.ServerUnavailableException
import kotlinx.coroutines.CancellationException

class CloudRecipesDataStore(
    private val connectionManager: ConnectionManager,
    private val recipesService: RecipesService) :
    RecipesDataStore {
    override suspend fun tryGetRecipes(
        query: String?,
        offset: Int,
        number: Int,
        sort: String
    ): List<RecipeEntity> =
        if (connectionManager.isNetworkAvailable()) {
            try {
                recipesService.getRecipes(query, offset, number, sort = sort).body()!!.results
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