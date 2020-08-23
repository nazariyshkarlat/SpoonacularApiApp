package com.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.domain.entity.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = RecipeAnalyzedInstructionsEntity.TABLE_NAME)
data class RecipeAnalyzedInstructionsEntity(@PrimaryKey(autoGenerate = false) @SerializedName("id")var id: Long, @SerializedName("analyzed_instructions")val analyzedInstructions: List<RecipeAnalyzedInstructionEntity>){
    companion object {
        const val TABLE_NAME = "recipes_analyzed_instructions"
    }
}

data class RecipeAnalyzedInstructionEntity(@SerializedName("steps")val steps: List<StepEntity>)

data class StepEntity(@SerializedName("number")val number: Int,
                      @SerializedName("step")val text: String,
                      @SerializedName("ingredients")val ingredients: List<IngredientEntity>,
                      @SerializedName("equipment")val equipment: List<EquipmentEntity>)

data class IngredientEntity(@SerializedName("id")val id: Long,
                           @SerializedName("image")val image: String?,
                           @SerializedName("name")val name: String)

data class EquipmentEntity(@SerializedName("id")val id: Long,
                           @SerializedName("image")val image: String?,
                           @SerializedName("name")val name: String)

fun RecipeAnalyzedInstructionsEntity.toBusinessEntity() = RecipeAnalyzedInstructionsData(id, analyzedInstructions.map { it.toBusinessEntity() })

private fun RecipeAnalyzedInstructionEntity.toBusinessEntity() = RecipeAnalyzedInstructionData(steps.map { it.toBusinessEntity() })

private fun StepEntity.toBusinessEntity() = StepData(number, text, ingredients.filter { !it.image.isNullOrEmpty() }.map { it.toBusinessEntity() }, equipment.filter { !it.image.isNullOrEmpty() }.map { it.toBusinessEntity() })

private fun IngredientEntity.toBusinessEntity() = IngredientData(id, image!!.ingredientNameToImageLink(), name)

private fun EquipmentEntity.toBusinessEntity() = EquipmentData(id, image!!.equipmentNameToImageLink(), name)

private fun String.ingredientNameToImageLink() = "https://spoonacular.com/cdn/ingredients_100x100/${this}"

private fun String.equipmentNameToImageLink() = "https://spoonacular.com/cdn/equipment_100x100/${this}"

fun RecipeAnalyzedInstructionsData.toRawEntity() = RecipeAnalyzedInstructionsEntity(recipeId, analyzedInstructions.map { it.toRawEntity() })

private fun RecipeAnalyzedInstructionData.toRawEntity() = RecipeAnalyzedInstructionEntity(steps.map { it.toRawEntity() })

private fun StepData.toRawEntity() = StepEntity(number, text, ingredients.map { it.toRawEntity() }, equipment.map { it.toRawEntity() })

private fun IngredientData.toRawEntity() = IngredientEntity(id, imageLink.imageLinkToIngredientName(), ingredientName)

private fun EquipmentData.toRawEntity() = EquipmentEntity(id, imageLink.imageLinkToEquipmentName(), equipmentName)

private fun String.imageLinkToIngredientName() = this.split('/').last()

private fun String.imageLinkToEquipmentName() = this.split('/').last()

