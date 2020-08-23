package com.app.fruitcocktail.di.entities_di

import com.app.data.net.RecipesService
import com.app.data.repository.RecipesRepositoryImpl
import com.app.data.repository.datasource.recipes.CacheRecipesDataStore
import com.app.data.repository.datasource.recipes.CloudRecipesDataStore
import com.app.data.repository.datasource.recipes.RecipesDataStoreFactoryImpl
import com.app.domain.repository.RecipesAnalyzedInstructionsRepository
import com.app.domain.use_cases.delete_recipe.DeleteRecipeUseCase
import com.app.domain.repository.RecipesRepository
import com.app.domain.repository.RecipesSummaryRepository
import com.app.domain.use_cases.check_is_saved.CheckRecipeIsSavedUseCase
import com.app.domain.use_cases.check_is_saved.CheckRecipeIsSavedUserCaseImpl
import com.app.domain.use_cases.delete_recipe.DeleteRecipeUseCaseImpl
import com.app.domain.use_cases.get_recipes.GetRecipesUseCase
import com.app.domain.use_cases.get_recipes.GetRecipesUseCaseImpl
import com.app.domain.use_cases.save_recipe.SaveRecipeUseCase
import com.app.domain.use_cases.save_recipe.SaveRecipeUseCaseImpl
import com.app.fruitcocktail.di.DI
import com.app.fruitcocktail.di.DatabaseModule
import com.app.fruitcocktail.di.NetworkModule

object RecipesModule {
    
    private lateinit var config: DI.Config

    private var repository: RecipesRepository? = null
    private var recipesUseCase: GetRecipesUseCase? = null
    private var saveRecipeUseCase: SaveRecipeUseCase? = null
    private var deleteRecipeUseCase: DeleteRecipeUseCase? = null
    private var checkRecipeIsSavedUseCase: CheckRecipeIsSavedUseCase? = null

    fun initialize(configuration: DI.Config = DI.Config.RELEASE) {
        config = configuration
    }

    fun getGetRecipesUseCaseImpl(): GetRecipesUseCase {
        if (config == DI.Config.RELEASE && recipesUseCase == null)
            recipesUseCase =
                makeGetRecipesUseCase(
                    getRecipesRepository()
                )
        return recipesUseCase!!
    }
    
    fun getSaveRecipeUseCaseImpl() : SaveRecipeUseCase {
        if (config == DI.Config.RELEASE && saveRecipeUseCase == null)
            saveRecipeUseCase =
                makeSaveRecipeUseCase(
                    getRecipesRepository(),
                    RecipesSummaryModule.getRecipesSummaryRepository(),
                    RecipesAnalyzedInstructionsModule.getRecipesAnalyzedInstructionsRepository()
                )
        return saveRecipeUseCase!!
    }

    fun getDeleteRecipeUseCaseImpl() : DeleteRecipeUseCase {
        if (config == DI.Config.RELEASE && deleteRecipeUseCase == null)
            deleteRecipeUseCase =
                makeDeleteRecipesUseCase(
                    getRecipesRepository(),
                    RecipesSummaryModule.getRecipesSummaryRepository(),
                    RecipesAnalyzedInstructionsModule.getRecipesAnalyzedInstructionsRepository()
                )
        return deleteRecipeUseCase!!
    }

    fun getCheckRecipeIsSavedUseCaseImpl() : CheckRecipeIsSavedUseCase {
        if (config == DI.Config.RELEASE && checkRecipeIsSavedUseCase == null)
            checkRecipeIsSavedUseCase =
                makeCheckRecipeIsSavedUseCase(
                    getRecipesRepository()
                )
        return checkRecipeIsSavedUseCase!!
    }

    private fun getRecipesRepository(): RecipesRepository {
        if (repository == null)
            repository = RecipesRepositoryImpl(
                getRecipesDataStoreFactory(),
                NetworkModule.connectionManager
            )
        return repository!!
    }

    private fun makeGetRecipesUseCase(repository: RecipesRepository) =
        GetRecipesUseCaseImpl(
            repository
        )

    private fun makeSaveRecipeUseCase(recipesRepository: RecipesRepository, recipesSummaryRepository: RecipesSummaryRepository, recipesAnalyzedInstructionsRepository: RecipesAnalyzedInstructionsRepository) =
        SaveRecipeUseCaseImpl(
            recipesRepository, recipesSummaryRepository, recipesAnalyzedInstructionsRepository
        )

    private fun makeDeleteRecipesUseCase(recipesRepository: RecipesRepository, recipesSummaryRepository: RecipesSummaryRepository, recipesAnalyzedInstructionsRepository: RecipesAnalyzedInstructionsRepository) =
        DeleteRecipeUseCaseImpl(
            recipesRepository, recipesSummaryRepository, recipesAnalyzedInstructionsRepository
        )

    private fun makeCheckRecipeIsSavedUseCase(recipesRepository: RecipesRepository) =
        CheckRecipeIsSavedUserCaseImpl(
            recipesRepository
        )
    
    private fun getRecipesDataStoreFactory() =
        RecipesDataStoreFactoryImpl(
            getCloudRecipesDataStore(),
            getCacheRecipesDataStore()
        )

    private fun getCloudRecipesDataStore() =
        CloudRecipesDataStore(
            NetworkModule.connectionManager,
            NetworkModule.getService(RecipesService::class.java)
        )

    private fun getCacheRecipesDataStore() =
        CacheRecipesDataStore(
            DatabaseModule.appDatabase.getRecipesListDao()
        )
}