package com.app.data.repository.datasource.recipe_analyzed_instructions

class RecipeAnalyzedInstructionsDataStoreFactoryImpl(
    private val cloudRecipeAnalyzedInstructionsDataStore: CloudRecipesAnalyzedInstructionsDataStore,
    private val cacheRecipeAnalyzedInstructionsDataStore: CacheRecipesAnalyzedInstructionsDataStore
) : RecipesAnalyzedInstructionsDataStoreFactory {

    override fun create(priority: RecipesAnalyzedInstructionsDataStoreFactory.Priority) =
        if (priority == RecipesAnalyzedInstructionsDataStoreFactory.Priority.CLOUD)
            cloudRecipeAnalyzedInstructionsDataStore
        else
            cacheRecipeAnalyzedInstructionsDataStore
}

interface RecipesAnalyzedInstructionsDataStoreFactory {

    enum class Priority {
        CLOUD,
        CACHE
    }

    fun create(priority: Priority): RecipesAnalyzedInstructionsDataStore
}