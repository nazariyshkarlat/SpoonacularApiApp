package com.app.domain.use_cases.get_recipes

import com.app.domain.use_cases.GetRecipesStatus

interface GetRecipesUseCase {

    val retryDelay: Long
        get() = 1000L

    val retryCount: Int
        get() = 3

    val perPage: Int
        get() = 20

    val sort: Sort
        get() = Sort.RANDOM

    suspend fun getRecipes(query: String?, pageNumber: Int, retryCount: Int = this.retryCount, sort: Sort = this.sort, areSavedRecipes: Boolean): GetRecipesStatus

    enum class Sort{
        RANDOM
    }
}

fun GetRecipesUseCase.Sort.toRaw() = when(this){
    GetRecipesUseCase.Sort.RANDOM -> "random"
}