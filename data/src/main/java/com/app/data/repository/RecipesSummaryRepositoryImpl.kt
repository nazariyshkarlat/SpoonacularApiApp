package com.app.data.repository

import com.app.data.entity.toBusinessEntity
import com.app.data.entity.toRawEntity
import com.app.data.net.connection.ConnectionManager
import com.app.data.repository.datasource.recipe_summary.CacheRecipesSummaryDataStore
import com.app.data.repository.datasource.recipe_summary.RecipesSummaryDataStoreFactory
import com.app.domain.entity.RecipeSummaryData
import com.app.domain.repository.RecipesSummaryRepository

class RecipesSummaryRepositoryImpl(private val recipeSummaryDataStoreFactory: RecipesSummaryDataStoreFactory, private val connectionManager: ConnectionManager) :
    RecipesSummaryRepository {

    override suspend fun getRecipeSummary(recipeId: Long, recipeIsSaved: Boolean): RecipeSummaryData =
        recipeSummaryDataStoreFactory.create(if(recipeIsSaved) RecipesSummaryDataStoreFactory.Priority.CACHE else RecipesSummaryDataStoreFactory.Priority.CLOUD).tryGetRecipeSummary(recipeId).toBusinessEntity()

    override suspend fun saveRecipeSummary(recipeSummaryData: RecipeSummaryData) {
        (recipeSummaryDataStoreFactory.create(RecipesSummaryDataStoreFactory.Priority.CACHE) as CacheRecipesSummaryDataStore).trySaveRecipeSummary(recipeSummaryData.toRawEntity())
    }

    override suspend fun deleteRecipeSummary(recipeId: Long) {
        (recipeSummaryDataStoreFactory.create(RecipesSummaryDataStoreFactory.Priority.CACHE) as CacheRecipesSummaryDataStore).tryDeleteRecipeSummary(recipeId)
    }

}