package com.app.fruitcocktail.di.entities_di


import com.app.data.net.RecipeSummaryService
import com.app.data.repository.RecipesSummaryRepositoryImpl
import com.app.data.repository.datasource.recipe_summary.CacheRecipesSummaryDataStore
import com.app.data.repository.datasource.recipe_summary.CloudRecipesSummaryDataStore
import com.app.data.repository.datasource.recipe_summary.RecipeSummaryDataStoreFactoryImpl
import com.app.domain.use_cases.get_recipe_summary.GetRecipeSummaryUseCase
import com.app.domain.use_cases.get_recipe_summary.GetRecipeSummaryUseCaseImpl
import com.app.domain.repository.RecipesSummaryRepository
import com.app.fruitcocktail.di.DI
import com.app.fruitcocktail.di.DatabaseModule
import com.app.fruitcocktail.di.NetworkModule

object RecipesSummaryModule {

    private lateinit var config: DI.Config

    private var repository: RecipesSummaryRepository? = null
    private var recipesUseCase: GetRecipeSummaryUseCase? = null

    fun initialize(configuration: DI.Config = DI.Config.RELEASE) {
        config = configuration
    }

    fun getGetRecipeSummaryUserCaseImpl(): GetRecipeSummaryUseCase {
        if (config == DI.Config.RELEASE && recipesUseCase == null)
            recipesUseCase =
                makeGetRecipeSummaryUserCase(
                    getRecipesSummaryRepository()
                )
        return recipesUseCase!!
    }

    fun getRecipesSummaryRepository(): RecipesSummaryRepository {
        if (repository == null)
            repository = RecipesSummaryRepositoryImpl(
                getRecipesSummaryDataStoreFactory(),
                NetworkModule.connectionManager
            )
        return repository!!
    }

    private fun makeGetRecipeSummaryUserCase(repository: RecipesSummaryRepository) =
        GetRecipeSummaryUseCaseImpl(
            repository
        )

    private fun getRecipesSummaryDataStoreFactory() =
        RecipeSummaryDataStoreFactoryImpl(
            getCloudRecipesSummaryDataStore(),
            getCacheRecipesSummaryDataStore()
        )

    private fun getCloudRecipesSummaryDataStore() =
        CloudRecipesSummaryDataStore(
            NetworkModule.connectionManager,
            NetworkModule.getService(RecipeSummaryService::class.java)
        )

    private fun getCacheRecipesSummaryDataStore() =
        CacheRecipesSummaryDataStore(
            DatabaseModule.appDatabase.getRecipeSummaryDao()
        )
}