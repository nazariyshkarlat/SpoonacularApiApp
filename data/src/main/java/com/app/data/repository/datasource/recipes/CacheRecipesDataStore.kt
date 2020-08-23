package com.app.data.repository.datasource.recipes

import com.app.data.database.recipes_list.RecipesListDao
import com.app.data.entity.RecipeEntity
import com.app.domain.exception.NetworkConnectionException
import kotlinx.coroutines.CancellationException

class CacheRecipesDataStore(
    private val recipesListDao: RecipesListDao
) : RecipesDataStore {
    override suspend fun tryGetRecipes(
        query: String?,
        offset: Int,
        number: Int,
        sort: String
    ): List<RecipeEntity> =
        try {
            println(recipesListDao.getRecipesList(offset, offset+number))
            recipesListDao.getRecipesList(offset, offset+number)
        } catch (exception: Exception) {
            if (exception is CancellationException)
                throw exception
            else
                throw NetworkConnectionException()
        }

    suspend fun saveRecipe(recipeEntity: RecipeEntity){
        recipesListDao.insertRecipe(recipeEntity)
    }

    suspend fun deleteRecipe(recipeId: Long){
        recipesListDao.deleteRecipe(recipeId)
    }

    suspend fun isRecipeSaved(recipeId: Long) = recipesListDao.checkRecipeSaved(recipeId)
}