package com.app.data.net

import com.app.data.entity.RecipeSummaryEntity
import com.app.data.entity.RecipesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeSummaryService{

    @GET("recipes/{recipe_id}/summary")
    suspend fun getRecipeSummary(
        @Path("recipe_id") recipeId: Long,
        @Query("apiKey")apiKey: String = com.app.data.net.apiKey) : Response<RecipeSummaryEntity>

}