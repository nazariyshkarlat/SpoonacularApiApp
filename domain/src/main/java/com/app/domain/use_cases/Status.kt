package com.app.domain.use_cases

import com.app.domain.entity.RecipeAnalyzedInstructionsData
import com.app.domain.entity.RecipeData
import com.app.domain.entity.RecipeSummaryData

sealed class GetRecipesStatus{
    object ServiceUnavailable : GetRecipesStatus()
    object NoConnection : GetRecipesStatus()
    object NoRecipes : GetRecipesStatus()
    class RecipesFound(val recipes: List<RecipeData>) : GetRecipesStatus()
}

sealed class GetRecipeSummaryStatus{
    object ServiceUnavailable : GetRecipeSummaryStatus()
    object NoConnection : GetRecipeSummaryStatus()
    class Success(val recipeSummaryData: RecipeSummaryData) : GetRecipeSummaryStatus()
}

sealed class GetRecipeAnalyzedInstructionsStatus{
    object ServiceUnavailable : GetRecipeAnalyzedInstructionsStatus()
    object NoConnection : GetRecipeAnalyzedInstructionsStatus()
    class Success(val recipeAnalyzedInstructions: RecipeAnalyzedInstructionsData) : GetRecipeAnalyzedInstructionsStatus()
}