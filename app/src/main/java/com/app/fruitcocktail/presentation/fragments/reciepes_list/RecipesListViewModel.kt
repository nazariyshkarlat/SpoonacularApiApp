package com.app.fruitcocktail.presentation.fragments.reciepes_list

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.domain.entity.RecipeData
import com.app.domain.use_cases.GetRecipesStatus
import com.app.fruitcocktail.R
import com.app.fruitcocktail.di.entities_di.RecipesModule
import com.app.fruitcocktail.extensions.launchExt
import com.app.fruitcocktail.presentation.model.RecipesListRecyclerItemUiModel
import com.app.fruitcocktail.presentation.model.toUiModel
import kotlinx.coroutines.Job

@ExperimentalStdlibApi
class RecipesListViewModel(private val isSavedRecipesFragment: Boolean) : ViewModel(){

    private val getRecipesUseCase = RecipesModule.getGetRecipesUseCaseImpl()

    val getRecipesState = MutableLiveData<RecipesResultState>()

    val errorState = MutableLiveData<Int>()
    val progressState = MutableLiveData(true)

    private var currentPageNumber = 1
    var currentQuery: String? = null

    val recyclerItems: ArrayList<RecipesListRecyclerItemUiModel> = ArrayList()

    private var getRecipesJob: Job? = null

    init {
        getRecipesList()
    }

    fun getRecipesList(query: String? = null, newPage: Boolean = false) {

        if(currentQuery != query)
            currentPageNumber = 1

        currentQuery = query

        if (newPage)
            currentPageNumber++

        if (currentPageNumber == 1)
            progressState.value = true

        getRecipesJob = viewModelScope.launchExt(getRecipesJob) {
            when (val result = getRecipesUseCase.getRecipes(
                query,
                currentPageNumber,
                areSavedRecipes = isSavedRecipesFragment
            )) {
                is GetRecipesStatus.NoConnection -> {
                    showError(R.string.no_connection)
                }
                is GetRecipesStatus.ServiceUnavailable -> {
                    showError(R.string.no_connection)
                }
                is GetRecipesStatus.NoRecipes -> {
                    if (currentPageNumber == 1){
                        progressState.postValue(false)
                        getRecipesState.postValue(RecipesResultState.NoResultsFound)
                    }else{
                        getRecipesState.postValue(RecipesResultState.NoMoreResultsFound)
                    }
                    hideError()
                }
                is GetRecipesStatus.RecipesFound -> {
                    recyclerItems.removeAll { it is RecipesListRecyclerItemUiModel.ProgressUiModel }

                    val recyclerNewItems = buildList {
                        addAll(result.recipes.map { it.toUiModel() })
                        if(canBeMoreItems(result.recipes)) add(RecipesListRecyclerItemUiModel.ProgressUiModel)
                    }

                    val recipesResultState: RecipesResultState
                    if (currentPageNumber == 1) {
                        recipesResultState = RecipesResultState.FirstResults(recyclerNewItems)
                        recyclerItems.clear()
                        recyclerItems.addAll(recyclerNewItems)
                    }else {
                        recipesResultState = RecipesResultState.MoreResults(
                            recyclerNewItems
                        )
                        recyclerItems.addAll(recyclerNewItems)
                    }

                    getRecipesState.postValue(recipesResultState)
                    progressState.postValue(false)
                    hideError()
                }
            }
        }
    }

    private fun showError(@StringRes stringId: Int){
        errorState.postValue(stringId)
    }

    private fun hideError(){
        errorState.postValue(-1)
    }

    private fun canBeMoreItems(receivedRecipesList: List<RecipeData>) = receivedRecipesList.size == getRecipesUseCase.perPage

    sealed class RecipesResultState {
        data class FirstResults(val results: List<RecipesListRecyclerItemUiModel>) : RecipesResultState()
        data class MoreResults(val results: List<RecipesListRecyclerItemUiModel>) : RecipesResultState()
        object NoResultsFound : RecipesResultState()
        object NoMoreResultsFound : RecipesResultState()
    }

}