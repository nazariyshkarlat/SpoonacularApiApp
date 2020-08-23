package com.app.data.net

import com.app.data.entity.RecipesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import com.app.data.net.apiKey

interface RecipesService{

    @GET("recipes/complexSearch")
    suspend fun getRecipes(@Query("query")query: String?,
                           @Query("offset")offset: Int,
                           @Query("number")number: Int,
                           @Query("sort")sort: String,
                           @Query("apiKey")apiKey: String = com.app.data.net.apiKey) : Response<RecipesResponse>

}