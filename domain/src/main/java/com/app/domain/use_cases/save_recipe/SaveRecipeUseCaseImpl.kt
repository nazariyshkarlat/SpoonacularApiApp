package com.app.domain.use_cases.save_recipe

import com.app.domain.entity.RecipeAnalyzedInstructionsData
import com.app.domain.entity.RecipeData
import com.app.domain.entity.RecipeSummaryData
import com.app.domain.repository.RecipesAnalyzedInstructionsRepository
import com.app.domain.repository.RecipesSummaryRepository
import com.app.domain.repository.RecipesRepository

class SaveRecipeUseCaseImpl(private val recipesRepository: RecipesRepository,
                            private val recipesSummaryRepository: RecipesSummaryRepository,
                            private val recipesAnalyzedInstructionsRepository: RecipesAnalyzedInstructionsRepository
) : SaveRecipeUseCase{
    override suspend fun saveRecipe(
        recipeData: RecipeData,
        recipeSummaryData: RecipeSummaryData,
        recipeAnalyzedInstructionsData: RecipeAnalyzedInstructionsData
    ) {
        recipesRepository.saveRecipe(recipeData)
        recipesSummaryRepository.saveRecipeSummary(recipeSummaryData)
        recipesAnalyzedInstructionsRepository.saveRecipeAnalyzedInstructions(recipeAnalyzedInstructionsData)
    }


}