package com.app.fruitcocktail.presentation.adapter.recipe

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.fruitcocktail.R
import com.app.fruitcocktail.extensions.replaceFragment
import com.app.fruitcocktail.presentation.fragments.recipe_info.RecipeInfoFragment
import com.app.fruitcocktail.presentation.model.RecipesListRecyclerItemUiModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.recipes_list_item.view.*


class RecipeViewHolder(view: View) : RecipesViewHolder(view) {

    @ExperimentalStdlibApi
    override fun bind(model: RecipesListRecyclerItemUiModel) {

        model as RecipesListRecyclerItemUiModel.RecipeUiModel

        with(itemView) {

            Glide
                .with(this)
                .load(model.imageLink)
                .apply(RequestOptions().placeholder(R.drawable.recipes_list_image_placeholder))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(recipesListItemImageView)

            recipesListItemTextView.text = model.title

            itemView.setOnClickListener {
                (context as AppCompatActivity).replaceFragment(RecipeInfoFragment.getInstance(model.recipeId, model.title), R.id.frameLayout)
            }
        }
    }

}