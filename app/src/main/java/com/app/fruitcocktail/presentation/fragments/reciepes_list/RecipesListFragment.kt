package com.app.fruitcocktail.presentation.fragments.reciepes_list

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.OneShotPreDrawListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.app.fruitcocktail.R
import com.app.fruitcocktail.extensions.replaceFragment
import com.app.fruitcocktail.presentation.adapter.recipe.RecipesAdapter
import com.app.fruitcocktail.presentation.fragments.BaseFragment
import com.app.fruitcocktail.presentation.model.RecipesListRecyclerItemUiModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.Behavior.DragCallback
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.recipes_list_layout.*


class RecipesListFragment : BaseFragment(R.layout.recipes_list_layout){

    private var snackbar: Snackbar? = null

    @SuppressLint("ClickableViewAccessibility")
    @ExperimentalStdlibApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T {
                return RecipesListViewModel(arguments?.getBoolean("ARE_SAVED_RECIPES") == true) as T
            }
        })[RecipesListViewModel::class.java]

        if(arguments?.getBoolean("ARE_SAVED_RECIPES") == true) {
            recipesListLayoutSearchLayout.visibility = View.GONE
            recipesListLayoutTitle.visibility = View.VISIBLE
            recipesListSearchLayoutImage.visibility = View.GONE
        }

        viewModel.getRecipesState.observe(
            viewLifecycleOwner,
            Observer {resultState->

                if(recipesListRecyclerView.adapter == null){
                    if(resultState is RecipesListViewModel.RecipesResultState.NoResultsFound){
                        showEmptyView(resources.getString(R.string.no_recipes_found))
                    }else{
                        val adapter = RecipesAdapter(
                            viewModel.recyclerItems,
                            viewModel)
                        recipesListRecyclerView.adapter = adapter
                    }
                }else {
                    when (resultState) {

                        is RecipesListViewModel.RecipesResultState.FirstResults -> {
                            hideEmptyView()
                            recipesListRecyclerView.adapter!!.notifyDataSetChanged()

                            recipesListRecyclerView.post{
                                recipesListRecyclerView.scrollY = 0
                            }
                        }
                        is RecipesListViewModel.RecipesResultState.MoreResults -> {
                            recipesListRecyclerView.adapter!!.notifyItemChanged(viewModel.recyclerItems.indexOfFirst { it is RecipesListRecyclerItemUiModel.ProgressUiModel })
                            val insertIndex = viewModel.recyclerItems.size
                            viewModel.recyclerItems.addAll(resultState.results)
                            recipesListRecyclerView.adapter!!.notifyItemRangeInserted(
                                insertIndex,
                                resultState.results.size-1
                            )
                        }
                        is RecipesListViewModel.RecipesResultState.NoMoreResultsFound -> {
                            recipesListRecyclerView.adapter!!.notifyItemRemoved(viewModel.recyclerItems.indexOfFirst { it is RecipesListRecyclerItemUiModel.ProgressUiModel })
                        }
                        is RecipesListViewModel.RecipesResultState.NoResultsFound -> {
                            showEmptyView(resources.getString(R.string.no_recipes_found))
                        }
                    }
                }
            })

        viewModel.progressState.observe(viewLifecycleOwner, Observer { progress ->
            hideEmptyView()
            if (progress) {
                recipesListRecyclerView.visibility = View.GONE
                recipesListProgressBar.visibility = View.VISIBLE
            } else {
                recipesListRecyclerView.visibility = View.VISIBLE
                recipesListProgressBar.visibility = View.GONE
            }
        })

        viewModel.errorState.observe(viewLifecycleOwner, Observer {

            if(it != -1) {
                snackbar = Snackbar.make(
                    view,
                    resources.getString(R.string.no_connection),
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction(resources.getString(R.string.retry)) {
                        viewModel.getRecipesList(viewModel.currentQuery)
                    }.setActionTextColor(ContextCompat.getColor(view.context, R.color.colorAccent))

                snackbar?.show()
            }else{
                snackbar?.dismiss()
            }
        })

        OneShotPreDrawListener.add(view){
            recipesListRecyclerView.layoutParams.height = view.measuredHeight - recipesListRecyclerView.translationY.toInt()
        }

        recipesListSearchLayoutImage.setOnClickListener {
            (activity as AppCompatActivity).replaceFragment(RecipesListFragment().apply {
                arguments = bundleOf("ARE_SAVED_RECIPES" to true)
            }, R.id.mainFrameLayout)
        }

        recipesListLayoutSearchLayout.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if(p0.toString().takeIf { it.isNotEmpty()} != viewModel.currentQuery)
                    viewModel.getRecipesList(p0.toString().takeIf { it.isNotEmpty() }?.trim())

                if(p0.isEmpty()) {
                    recipesListSearchLayoutImage.setImageResource(R.drawable.ic_bookmark_border)
                    recipesListSearchLayoutImage.setOnClickListener {
                        (activity as AppCompatActivity).replaceFragment(RecipesListFragment().apply {
                            arguments = bundleOf("ARE_SAVED_RECIPES" to true)
                        }, R.id.mainFrameLayout)
                    }
                }else{
                    recipesListSearchLayoutImage.setImageResource(R.drawable.ic_close)
                    recipesListSearchLayoutImage.setOnClickListener {
                        recipesListLayoutSearchLayout.text.clear()
                    }
                }
            }

        })

        recipesListRecyclerView.setOnTouchListener { v, _ ->
            val imm: InputMethodManager? =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(v.windowToken, 0)
            false
        }

        OneShotPreDrawListener.add(view) {
            removeAppBarDrag()
        }
    }

    override fun onDestroyView() {
        snackbar?.dismiss()
        super.onDestroyView()
    }

    private fun showEmptyView(text: String){
        recipesListRecyclerView.visibility = View.GONE
        recipesListText.visibility = View.VISIBLE
        recipesListText.text = text
    }


    private fun removeAppBarDrag(){
        val behavior = (recipesListLayoutAppBar.layoutParams as CoordinatorLayout.LayoutParams).behavior as AppBarLayout.Behavior?
        behavior!!.setDragCallback(object : DragCallback() {
            override fun canDrag(appBarLayout: AppBarLayout): Boolean {
                return false
            }
        })
    }

    private fun hideEmptyView(){
        recipesListText.visibility = View.GONE
        recipesListRecyclerView.visibility = View.VISIBLE
    }

}
