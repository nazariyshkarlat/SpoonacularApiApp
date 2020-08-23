package com.app.domain.entity

data class RecipeAnalyzedInstructionsData(val recipeId: Long, val analyzedInstructions: List<RecipeAnalyzedInstructionData>)

data class RecipeAnalyzedInstructionData(val steps: List<StepData>)

data class StepData(val number: Int,
                    val text: String,
                    val ingredients: List<IngredientData>,
                    val equipment: List<EquipmentData>)

data class IngredientData(val id: Long,
                          val imageLink: String,
                          val ingredientName: String)

data class EquipmentData(val id: Long,
                         val imageLink: String,
                         val equipmentName: String)