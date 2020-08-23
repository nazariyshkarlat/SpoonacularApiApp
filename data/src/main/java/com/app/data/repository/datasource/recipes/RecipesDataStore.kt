package com.app.data.repository.datasource.recipes

import com.app.data.entity.RecipeEntity

interface RecipesDataStore {

    suspend fun tryGetRecipes(query: String?, offset: Int, number: Int, sort: String): List<RecipeEntity>

}