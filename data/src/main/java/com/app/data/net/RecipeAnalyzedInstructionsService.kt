package com.app.data.net

import com.app.data.entity.RecipeAnalyzedInstructionEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeAnalyzedInstructionsService {

    @GET("recipes/{recipe_id}/analyzedInstructions")
    suspend fun getRecipeAnalyzedInstructions(
        @Path("recipe_id") recipeId: Long,
        @Query("apiKey")apiKey: String = com.app.data.net.apiKey) : Response<List<RecipeAnalyzedInstructionEntity>>
}