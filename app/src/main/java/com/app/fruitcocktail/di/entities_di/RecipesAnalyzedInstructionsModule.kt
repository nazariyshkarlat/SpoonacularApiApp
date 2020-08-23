package com.app.fruitcocktail.di.entities_di


import com.app.data.net.RecipeAnalyzedInstructionsService
import com.app.data.repository.RecipesAnalyzedInstructionsRepositoryImpl
import com.app.data.repository.datasource.recipe_analyzed_instructions.CacheRecipesAnalyzedInstructionsDataStore
import com.app.data.repository.datasource.recipe_analyzed_instructions.CloudRecipesAnalyzedInstructionsDataStore
import com.app.data.repository.datasource.recipe_analyzed_instructions.RecipeAnalyzedInstructionsDataStoreFactoryImpl
import com.app.domain.use_cases.get_recipe_analyzed_instructions.GetRecipeAnalyzedInstructionsUseCase
import com.app.domain.use_cases.get_recipe_analyzed_instructions.GetRecipeAnalyzedInstructionsUseCaseImpl
import com.app.domain.repository.RecipesAnalyzedInstructionsRepository
import com.app.fruitcocktail.di.DI
import com.app.fruitcocktail.di.DatabaseModule
import com.app.fruitcocktail.di.NetworkModule

object RecipesAnalyzedInstructionsModule {

    private lateinit var config: DI.Config

    private var repository: RecipesAnalyzedInstructionsRepository? = null
    private var recipesUseCase: GetRecipeAnalyzedInstructionsUseCase? = null

    fun initialize(configuration: DI.Config = DI.Config.RELEASE) {
        config = configuration
    }

    fun getGetRecipeAnalyzedInstructionsUseCaseImpl(): GetRecipeAnalyzedInstructionsUseCase {
        if (config == DI.Config.RELEASE && recipesUseCase == null)
            recipesUseCase =
                makeGetRecipeAnalyzedInstructionsUseCase(
                    getRecipesAnalyzedInstructionsRepository()
                )
        return recipesUseCase!!
    }

    fun getRecipesAnalyzedInstructionsRepository(): RecipesAnalyzedInstructionsRepository {
        if (repository == null)
            repository = RecipesAnalyzedInstructionsRepositoryImpl(
                getRecipesAnalyzedInstructionsDataStoreFactory(),
                NetworkModule.connectionManager
            )
        return repository!!
    }

    private fun makeGetRecipeAnalyzedInstructionsUseCase(repository: RecipesAnalyzedInstructionsRepository) =
        GetRecipeAnalyzedInstructionsUseCaseImpl(
            repository
        )

    private fun getRecipesAnalyzedInstructionsDataStoreFactory() =
        RecipeAnalyzedInstructionsDataStoreFactoryImpl(
            getCloudRecipesAnalyzedInstructionsDataStore(),
            getCacheRecipesAnalyzedInstructionsDataStore()
        )

    private fun getCloudRecipesAnalyzedInstructionsDataStore() =
        CloudRecipesAnalyzedInstructionsDataStore(
            NetworkModule.connectionManager,
            NetworkModule.getService(RecipeAnalyzedInstructionsService::class.java)
        )

    private fun getCacheRecipesAnalyzedInstructionsDataStore() =
        CacheRecipesAnalyzedInstructionsDataStore(
            DatabaseModule.appDatabase.getRecipeAnalyzedInstructionsDao()
        )

}