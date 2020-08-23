package com.app.data.repository

import com.app.data.entity.toBusinessEntity
import com.app.data.entity.toRawEntity
import com.app.data.net.connection.ConnectionManager
import com.app.data.repository.datasource.recipe_analyzed_instructions.CacheRecipesAnalyzedInstructionsDataStore
import com.app.data.repository.datasource.recipe_analyzed_instructions.RecipesAnalyzedInstructionsDataStoreFactory
import com.app.domain.entity.RecipeAnalyzedInstructionsData
import com.app.domain.repository.RecipesAnalyzedInstructionsRepository

class RecipesAnalyzedInstructionsRepositoryImpl(private val recipeAnalyzedInstructionsDataStoreFactory: RecipesAnalyzedInstructionsDataStoreFactory, private val connectionManager: ConnectionManager) :
    RecipesAnalyzedInstructionsRepository {

    override suspend fun getRecipeAnalyzedInstructions(recipeId: Long, recipeIsSaved: Boolean): RecipeAnalyzedInstructionsData =
        recipeAnalyzedInstructionsDataStoreFactory.create(if(recipeIsSaved) RecipesAnalyzedInstructionsDataStoreFactory.Priority.CACHE else RecipesAnalyzedInstructionsDataStoreFactory.Priority.CLOUD)
            .tryGetRecipeAnalyzedInstructions(recipeId).toBusinessEntity()

    override suspend fun saveRecipeAnalyzedInstructions(recipeAnalyzedInstructionsData: RecipeAnalyzedInstructionsData) {
        (recipeAnalyzedInstructionsDataStoreFactory.create(RecipesAnalyzedInstructionsDataStoreFactory.Priority.CACHE) as CacheRecipesAnalyzedInstructionsDataStore)
            .trySaveRecipeAnalyzedInstructions(recipeAnalyzedInstructionsData.toRawEntity())
    }

    override suspend fun deleteRecipeAnalyzedInstructions(recipeId: Long) {
        (recipeAnalyzedInstructionsDataStoreFactory.create(RecipesAnalyzedInstructionsDataStoreFactory.Priority.CACHE) as CacheRecipesAnalyzedInstructionsDataStore)
            .tryDeleteRecipeAnalyzedInstructions(recipeId)
    }

}
