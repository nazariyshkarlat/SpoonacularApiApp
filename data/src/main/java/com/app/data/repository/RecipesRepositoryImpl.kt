package com.app.data.repository

import com.app.data.entity.toBusinessEntity
import com.app.data.entity.toRawEntity
import com.app.data.net.connection.ConnectionManager
import com.app.data.repository.datasource.recipes.CacheRecipesDataStore
import com.app.data.repository.datasource.recipes.RecipesDataStoreFactory
import com.app.domain.entity.RecipeData
import com.app.domain.use_cases.get_recipes.GetRecipesUseCase
import com.app.domain.use_cases.get_recipes.toRaw
import com.app.domain.repository.RecipesRepository

class RecipesRepositoryImpl(private val recipesDataStoreFactory: RecipesDataStoreFactory, private val connectionManager: ConnectionManager) : RecipesRepository{
    override suspend fun getRecipes(
        query: String?,
        perPage: Int,
        pageNumber: Int,
        sort: GetRecipesUseCase.Sort,
        areSavedRecipes: Boolean
    ): List<RecipeData> =
        recipesDataStoreFactory.create(if(areSavedRecipes) RecipesDataStoreFactory.Priority.CACHE else RecipesDataStoreFactory.Priority.CLOUD).tryGetRecipes(query, perPage*(pageNumber-1), perPage, sort.toRaw()).map { it.toBusinessEntity() }

    override suspend fun saveRecipe(recipeData: RecipeData) {
        (recipesDataStoreFactory.create(
            RecipesDataStoreFactory.Priority.CACHE) as CacheRecipesDataStore)
            .saveRecipe(recipeData.toRawEntity())
    }

    override suspend fun deleteRecipe(recipeId: Long) {
        (recipesDataStoreFactory.create(
            RecipesDataStoreFactory.Priority.CACHE) as CacheRecipesDataStore)
            .deleteRecipe(recipeId)
    }

    override suspend fun isRecipeSaved(recipeId: Long) = (recipesDataStoreFactory.create(
        RecipesDataStoreFactory.Priority.CACHE) as CacheRecipesDataStore)
        .isRecipeSaved(recipeId)

}