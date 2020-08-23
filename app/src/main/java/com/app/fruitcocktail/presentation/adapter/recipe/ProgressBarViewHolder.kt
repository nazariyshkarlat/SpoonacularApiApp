package com.app.fruitcocktail.presentation.adapter.recipe

import android.view.View
import com.app.fruitcocktail.presentation.model.RecipesListRecyclerItemUiModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.recipes_list_item.view.*

class ProgressBarViewHolder(view: View, private val requestMoreItemsCallback: RequestMoreItemsCallback) : RecipesViewHolder(view) {

    override fun bind(model: RecipesListRecyclerItemUiModel) {
        model as RecipesListRecyclerItemUiModel.ProgressUiModel
        requestMoreItemsCallback.onRequestMoreItems()
    }

    interface RequestMoreItemsCallback {
        fun onRequestMoreItems()
    }

}