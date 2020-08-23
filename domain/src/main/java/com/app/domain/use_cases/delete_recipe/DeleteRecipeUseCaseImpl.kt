package com.app.domain.use_cases.delete_recipe

import com.app.domain.repository.RecipesAnalyzedInstructionsRepository
import com.app.domain.repository.RecipesSummaryRepository
import com.app.domain.repository.RecipesRepository

class DeleteRecipeUseCaseImpl(private val recipesRepository: RecipesRepository,
                              private val recipesSummaryRepository: RecipesSummaryRepository,
                              private val recipesAnalyzedInstructionsRepository: RecipesAnalyzedInstructionsRepository) : DeleteRecipeUseCase{
    override suspend fun deleteRecipe(recipeId: Long) {
        recipesRepository.deleteRecipe(recipeId)
        recipesSummaryRepository.deleteRecipeSummary(recipeId)
        recipesAnalyzedInstructionsRepository.deleteRecipeAnalyzedInstructions(recipeId)
    }
}