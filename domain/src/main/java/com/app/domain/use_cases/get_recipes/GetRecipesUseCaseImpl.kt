package com.app.domain.use_cases.get_recipes

import com.app.domain.exception.NetworkConnectionException
import com.app.domain.exception.ServerUnavailableException
import com.app.domain.use_cases.GetRecipesStatus
import com.app.domain.repository.RecipesRepository
import kotlinx.coroutines.delay

class GetRecipesUseCaseImpl(private val recipesRepository: RecipesRepository) :
    GetRecipesUseCase {

    override suspend fun getRecipes(query: String?, pageNumber: Int, retryCount: Int, sort: GetRecipesUseCase.Sort, areSavedRecipes: Boolean): GetRecipesStatus =
        try {
            val recipes = recipesRepository.getRecipes(query, perPage, pageNumber, sort, areSavedRecipes)
            when {
                recipes.isEmpty() -> GetRecipesStatus.NoRecipes
                else -> GetRecipesStatus.RecipesFound(recipes)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is NetworkConnectionException || e is ServerUnavailableException) {
                if (retryCount == 0) {
                    if (e is NetworkConnectionException) {
                        GetRecipesStatus.NoConnection
                    } else
                        GetRecipesStatus.ServiceUnavailable
                } else {
                    if (e is NetworkConnectionException)
                        delay(retryDelay)
                    getRecipes(query, pageNumber, retryCount - 1, areSavedRecipes = areSavedRecipes)
                }
            } else
                throw e
        }

}