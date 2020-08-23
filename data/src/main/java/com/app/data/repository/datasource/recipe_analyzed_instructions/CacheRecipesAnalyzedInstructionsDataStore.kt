package com.app.data.repository.datasource.recipe_analyzed_instructions

import com.app.data.database.recipe_analyzed_instructions.RecipesAnalyzedInstructionsDao
import com.app.data.entity.RecipeAnalyzedInstructionsEntity
import com.app.domain.exception.NetworkConnectionException
import kotlinx.coroutines.CancellationException

class CacheRecipesAnalyzedInstructionsDataStore(
    private val recipesAnalyzedInstructionsDao: RecipesAnalyzedInstructionsDao
) : RecipesAnalyzedInstructionsDataStore {

    override suspend fun tryGetRecipeAnalyzedInstructions(recipeId: Long): RecipeAnalyzedInstructionsEntity =
        try {
            recipesAnalyzedInstructionsDao.getRecipeAnalyzedInstructions(recipeId)
        } catch (exception: Exception) {
            if (exception is CancellationException)
                throw exception
            else
                throw NetworkConnectionException()
        }

    suspend fun trySaveRecipeAnalyzedInstructions(recipeAnalyzedInstructionsEntity: RecipeAnalyzedInstructionsEntity){
        recipesAnalyzedInstructionsDao.insertRecipeAnalyzedInstructions(recipeAnalyzedInstructionsEntity)
    }

    suspend fun tryDeleteRecipeAnalyzedInstructions(recipeId: Long){
        recipesAnalyzedInstructionsDao.deleteRecipeAnalyzedInstructions(recipeId)
    }
}