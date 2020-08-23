package com.app.fruitcocktail.presentation.fragments.recipe_info

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.domain.entity.RecipeAnalyzedInstructionsData
import com.app.domain.entity.RecipeData
import com.app.domain.entity.RecipeSummaryData
import com.app.domain.use_cases.GetRecipeAnalyzedInstructionsStatus
import com.app.domain.use_cases.GetRecipeSummaryStatus
import com.app.fruitcocktail.R
import com.app.fruitcocktail.di.entities_di.RecipesAnalyzedInstructionsModule
import com.app.fruitcocktail.di.entities_di.RecipesModule
import com.app.fruitcocktail.di.entities_di.RecipesSummaryModule
import com.app.fruitcocktail.extensions.launchExt
import com.app.fruitcocktail.presentation.model.RecipeInfoUiModel
import com.app.fruitcocktail.presentation.model.toUiModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import java.util.*

class RecipeInfoViewModel(private val recipeId: Long, private val recipeName: String) : ViewModel(){

    val getRecipeInfoState = MutableLiveData<RecipeInfoUiModel>()
    val progressState = MutableLiveData(true)
    val errorState = MutableLiveData<Int>()
    val fabState = MutableLiveData<Boolean>()

    private val getRecipeSummaryUseCase = RecipesSummaryModule.getGetRecipeSummaryUserCaseImpl()
    private val getRecipeAnalyzedInstructionsUseCase = RecipesAnalyzedInstructionsModule.getGetRecipeAnalyzedInstructionsUseCaseImpl()
    private val deleteRecipeUseCase = RecipesModule.getDeleteRecipeUseCaseImpl()
    private val saveRecipeUseCase = RecipesModule.getSaveRecipeUseCaseImpl()
    private val checkRecipeIsSavedUseCase = RecipesModule.getCheckRecipeIsSavedUseCaseImpl()

    private var getRecipeInfoJob: Job? = null
    private var saveRecipeJob: Job? = null

    lateinit var recipeData: RecipeData
    lateinit var recipeSummaryData: RecipeSummaryData
    lateinit var recipeAnalyzedInstructionsData: RecipeAnalyzedInstructionsData
    var isRecipeSaved = false

    init {
        getRecipeInfo()
    }

    fun getRecipeInfo(){
        val imageLink = formImageLink(recipeId)
        getRecipeInfoJob = viewModelScope.launchExt(getRecipeInfoJob){
            checkRecipeIsSaved()
            val recipeSummaryDeferred = async{ getRecipeSummary(recipeId) }
            val recipeAnalyzedInstructionsDeferred = async { getRecipeAnalyzedInstructions(recipeId)  }
            val recipeSummaryStatus = recipeSummaryDeferred.await()
            val recipeAnalyzedInstructionsStatus  = recipeAnalyzedInstructionsDeferred.await()
            if(recipeSummaryStatus is GetRecipeSummaryStatus.Success && recipeAnalyzedInstructionsStatus is GetRecipeAnalyzedInstructionsStatus.Success){
                recipeData = RecipeData(recipeId, recipeName, formRecipesListSmallImageLink(recipeId))
                recipeSummaryData = recipeSummaryStatus.recipeSummaryData
                recipeAnalyzedInstructionsData = recipeAnalyzedInstructionsStatus.recipeAnalyzedInstructions
                getRecipeInfoState.postValue(RecipeInfoUiModel(recipeId,
                    recipeName,
                    imageLink,
                    recipeSummaryStatus.recipeSummaryData.toUiModel(),
                    recipeAnalyzedInstructionsStatus.recipeAnalyzedInstructions.toUiModel()))
                hideError()
            }else{
                showError(R.string.no_connection)
            }
        }
    }

    fun saveRecipe(){
        isRecipeSaved = true
        if(getRecipeInfoState.value != null) {
            saveRecipeJob = viewModelScope.launchExt(saveRecipeJob) {
                saveRecipeUseCase.saveRecipe(
                    recipeData.apply { date = Date() },
                    recipeSummaryData,
                    recipeAnalyzedInstructionsData
                )
            }
        }
    }

    fun deleteRecipe(){
        isRecipeSaved = false
        if(getRecipeInfoState.value != null) {
            saveRecipeJob = viewModelScope.launchExt(saveRecipeJob) {
                deleteRecipeUseCase.deleteRecipe(
                    recipeId
                )
            }
        }
    }

    private suspend fun checkRecipeIsSaved(){
        isRecipeSaved = checkRecipeIsSavedUseCase.recipeIsSaved(
            recipeId
        )
    }


    private suspend fun getRecipeSummary(recipeId: Long) = getRecipeSummaryUseCase.getRecipeSummary(recipeId, recipeIsSaved = isRecipeSaved)
    private suspend fun getRecipeAnalyzedInstructions(recipeId: Long) = getRecipeAnalyzedInstructionsUseCase.getRecipeAnalyzedInstructions(recipeId, recipeIsSaved = isRecipeSaved)

    private fun showError(@StringRes stringId: Int){
        errorState.postValue(stringId)
    }

    private fun hideError(){
        errorState.postValue(-1)
    }


    private fun formImageLink(recipeId: Long) = "https://spoonacular.com/recipeImages/${recipeId}-556x370.jpg"

    private fun formRecipesListSmallImageLink(recipeId: Long) = "https://spoonacular.com/recipeImages/${recipeId}-312x231.jpg"


    fun showFAB() {
        fabState.value = true
    }

    fun hideFAB() {
        fabState.value = false
    }
}