package com.app.fruitcocktail.presentation.adapter.recipe

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.fruitcocktail.R
import com.app.fruitcocktail.extensions.makeView
import com.app.fruitcocktail.extensions.replaceFragment
import com.app.fruitcocktail.presentation.fragments.reciepes_list.RecipesListViewModel
import com.app.fruitcocktail.presentation.fragments.recipe_info.RecipeInfoFragment
import com.app.fruitcocktail.presentation.model.RecipesListRecyclerItemUiModel

class RecipesAdapter @ExperimentalStdlibApi constructor(private val recipesListItems: List<RecipesListRecyclerItemUiModel>, private val recipesListViewModel: RecipesListViewModel) :
    RecyclerView.Adapter<RecipesViewHolder>(), ProgressBarViewHolder.RequestMoreItemsCallback {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesViewHolder =
        when(viewType) {
            RecipesItemsViewType.RECIPE.ordinal -> RecipeViewHolder(parent.makeView(R.layout.recipes_list_item))
            RecipesItemsViewType.PROGRESS_BAR.ordinal -> ProgressBarViewHolder(parent.makeView(R.layout.recipes_list_progress_item), this)
            else -> throw UnsupportedOperationException("unknown type of item")
        }

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int) {
        holder.bind(recipesListItems[position])
    }

    override fun getItemCount() = recipesListItems.size

    override fun getItemViewType(position: Int): Int =
        when (recipesListItems[position]) {
            is RecipesListRecyclerItemUiModel.RecipeUiModel-> RecipesItemsViewType.RECIPE.ordinal
            is RecipesListRecyclerItemUiModel.ProgressUiModel -> RecipesItemsViewType.PROGRESS_BAR.ordinal
        }

    @ExperimentalStdlibApi
    override fun onRequestMoreItems() {
        recipesListViewModel.getRecipesList(recipesListViewModel.currentQuery, true)
    }

}


abstract class RecipesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun bind(model: RecipesListRecyclerItemUiModel)
}

private enum class RecipesItemsViewType {
    RECIPE,
    PROGRESS_BAR
}