package com.app.fruitcocktail.presentation.model

import com.app.domain.entity.RecipeData

sealed class RecipesListRecyclerItemUiModel {
    data class RecipeUiModel(val recipeId: Long, val title: String, val imageLink: String) : RecipesListRecyclerItemUiModel()
    object ProgressUiModel : RecipesListRecyclerItemUiModel()
}

fun RecipeData.toUiModel() = RecipesListRecyclerItemUiModel.RecipeUiModel(recipeId, title, imageLink)