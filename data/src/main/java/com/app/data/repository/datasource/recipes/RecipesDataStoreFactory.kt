package com.app.data.repository.datasource.recipes

class RecipesDataStoreFactoryImpl(
    private val cloudRecipesDataStore: CloudRecipesDataStore,
    private val cacheRecipesDataStore: CacheRecipesDataStore
) : RecipesDataStoreFactory {

    override fun create(priority: RecipesDataStoreFactory.Priority) =
        if (priority == RecipesDataStoreFactory.Priority.CLOUD)
            cloudRecipesDataStore
        else
            cacheRecipesDataStore
}

interface RecipesDataStoreFactory {

    enum class Priority {
        CLOUD,
        CACHE
    }

    fun create(priority: Priority): RecipesDataStore
}