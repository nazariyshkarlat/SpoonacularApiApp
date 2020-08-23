package com.app.fruitcocktail.presentation.model

import com.app.data.entity.RecipeAnalyzedInstructionsEntity
import com.app.data.entity.toBusinessEntity
import com.app.domain.entity.*

data class RecipeInfoUiModel(val recipeId: Long,
                             val recipeName: String,
                             val imageLink: String,
                             val summary: RecipeSummaryUiModel,
                             val analyzedInstructions: RecipeAnalyzedInstructionsUiModel)

data class RecipeSummaryUiModel(val summary: String)

data class RecipeAnalyzedInstructionsUiModel(val analyzedInstructions: List<RecipeAnalyzedInstructionUiModel>)

data class RecipeAnalyzedInstructionUiModel(val steps: List<StepUiModel>)

data class StepUiModel(val number: String,
                    val text: String,
                    val ingredients: List<IngredientUiModel>,
                    val equipment: List<EquipmentUiModel>)

data class IngredientUiModel(val id: Long,
                          val imageLink: String,
                          val ingredientName: String)

data class EquipmentUiModel(val id: Long,
                         val imageLink: String,
                         val equipmentName: String)

fun RecipeAnalyzedInstructionsData.toUiModel() = RecipeAnalyzedInstructionsUiModel(analyzedInstructions.map { it.toUiModel() })

fun RecipeSummaryData.toUiModel() = RecipeSummaryUiModel(summary)

private fun RecipeAnalyzedInstructionData.toUiModel() = RecipeAnalyzedInstructionUiModel(steps.map { it.toUiModel() })

private fun StepData.toUiModel() = StepUiModel(number.toString(), text, ingredients.map { it.toUiModel() }, equipment.map { it.toUiModel() })

private fun IngredientData.toUiModel() = IngredientUiModel(id, imageLink, ingredientName)

private fun EquipmentData.toUiModel() = EquipmentUiModel(id, imageLink, equipmentName)