package com.app.domain.use_cases.get_recipe_analyzed_instructions

import com.app.domain.exception.NetworkConnectionException
import com.app.domain.exception.ServerUnavailableException
import com.app.domain.use_cases.GetRecipeAnalyzedInstructionsStatus
import com.app.domain.repository.RecipesAnalyzedInstructionsRepository
import kotlinx.coroutines.delay

class GetRecipeAnalyzedInstructionsUseCaseImpl(private val recipesAnalyzedInstructionsRepository: RecipesAnalyzedInstructionsRepository) :
    GetRecipeAnalyzedInstructionsUseCase {

    override suspend fun getRecipeAnalyzedInstructions(recipeId: Long, retryCount: Int, recipeIsSaved: Boolean): GetRecipeAnalyzedInstructionsStatus =
        try {
            GetRecipeAnalyzedInstructionsStatus.Success(recipesAnalyzedInstructionsRepository.getRecipeAnalyzedInstructions(recipeId, recipeIsSaved))
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is NetworkConnectionException || e is ServerUnavailableException) {
                if (retryCount == 0) {
                    if (e is NetworkConnectionException) {
                        GetRecipeAnalyzedInstructionsStatus.NoConnection
                    } else
                        GetRecipeAnalyzedInstructionsStatus.ServiceUnavailable
                } else {
                    if (e is NetworkConnectionException)
                        delay(retryDelay)
                    getRecipeAnalyzedInstructions(recipeId, retryCount - 1, recipeIsSaved)
                }
            } else
                throw e
        }

}