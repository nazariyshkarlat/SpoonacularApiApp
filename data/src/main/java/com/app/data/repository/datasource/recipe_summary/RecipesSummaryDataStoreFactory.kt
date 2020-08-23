package com.app.data.repository.datasource.recipe_summary

class RecipeSummaryDataStoreFactoryImpl(
    private val cloudRecipeSummaryDataStore: CloudRecipesSummaryDataStore,
    private val cacheRecipeSummaryDataStore: CacheRecipesSummaryDataStore
) : RecipesSummaryDataStoreFactory {

    override fun create(priority: RecipesSummaryDataStoreFactory.Priority) =
        if (priority == RecipesSummaryDataStoreFactory.Priority.CLOUD)
            cloudRecipeSummaryDataStore
        else
            cacheRecipeSummaryDataStore
}

interface RecipesSummaryDataStoreFactory {

    enum class Priority {
        CLOUD,
        CACHE
    }

    fun create(priority: Priority): RecipesSummaryDataStore
}