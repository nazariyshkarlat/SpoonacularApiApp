package com.app.domain.use_cases.get_recipe_summary

import com.app.domain.exception.NetworkConnectionException
import com.app.domain.exception.ServerUnavailableException
import com.app.domain.use_cases.GetRecipeSummaryStatus
import com.app.domain.repository.RecipesSummaryRepository
import kotlinx.coroutines.delay

class GetRecipeSummaryUseCaseImpl(private val recipesSummaryRepository: RecipesSummaryRepository) :
    GetRecipeSummaryUseCase {

    override suspend fun getRecipeSummary(recipeId: Long, retryCount: Int, recipeIsSaved: Boolean): GetRecipeSummaryStatus =
        try {
            GetRecipeSummaryStatus.Success(recipesSummaryRepository.getRecipeSummary(recipeId, recipeIsSaved))
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is NetworkConnectionException || e is ServerUnavailableException) {
                if (retryCount == 0) {
                    if (e is NetworkConnectionException) {
                        GetRecipeSummaryStatus.NoConnection
                    } else
                        GetRecipeSummaryStatus.ServiceUnavailable
                } else {
                    if (e is NetworkConnectionException)
                        delay(retryDelay)
                    getRecipeSummary(recipeId, retryCount - 1, recipeIsSaved)
                }
            } else
                throw e
        }

}