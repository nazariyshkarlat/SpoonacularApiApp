package com.app.domain.use_cases.get_recipe_summary

import com.app.domain.use_cases.GetRecipeSummaryStatus

interface GetRecipeSummaryUseCase {

    val retryDelay: Long
        get() = 1000L

    val retryCount: Int
        get() = 3

    suspend fun getRecipeSummary(recipeId: Long, retryCount: Int = this.retryCount, recipeIsSaved: Boolean): GetRecipeSummaryStatus
}