package com.app.fruitcocktail.presentation.fragments.recipe_info

import android.os.Bundle
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.marginBottom
import androidx.core.view.updatePadding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.app.fruitcocktail.R
import com.app.fruitcocktail.extensions.*
import com.app.fruitcocktail.presentation.fragments.BaseFragment
import com.app.fruitcocktail.presentation.listeners.FabScrollListener
import com.app.fruitcocktail.presentation.model.RecipeAnalyzedInstructionsUiModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.analyzed_instructions_step_flexbox_item_layout.view.*
import kotlinx.android.synthetic.main.analyzed_instructions_step_layout.view.*
import kotlinx.android.synthetic.main.recipe_info_layout.*


@ExperimentalStdlibApi
class RecipeInfoFragment : BaseFragment(R.layout.recipe_info_layout), ViewTreeObserver.OnScrollChangedListener, FabScrollListener{

    companion object{
        fun getInstance(recipeId: Long, recipeName: String) = RecipeInfoFragment().apply {
            arguments = bundleOf("RECIPE_ID" to recipeId, "RECIPE_NAME" to recipeName)
        }
    }

    private var snackbar: Snackbar? = null
    private var toolbarHeight = 0
    lateinit var viewModel: RecipeInfoViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T {
                    return RecipeInfoViewModel(arguments!!.getLong("RECIPE_ID"), arguments!!.getString("RECIPE_NAME")!!) as T
                }
            })[RecipeInfoViewModel::class.java]

        viewModel.getRecipeInfoState.observe(viewLifecycleOwner, Observer {
            recipeInfoLayoutRecipeTitle.text = it.recipeName

            view.post{
                recipeInfoLayoutScrollView.updatePadding(top = recipeInfoLayoutRecipeTitle.measuredHeight)
                viewModel.progressState.postValue(false)
                toolbarHeight = recipeInfoLayoutRecipeTitle.measuredHeight

                recipeInfoLayoutFAB.visibility = View.VISIBLE
                recipeInfoLayoutFAB.isSelected = viewModel.isRecipeSaved
            }

            Glide
                .with(this)
                .load(it.imageLink)
                .transform(RoundedCorners(8.dpToPx()))
                .apply(RequestOptions().placeholder(R.drawable.recipe_info_image_placeholder))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(recipeInfoLayoutImageView)

            recipeInfoLayoutRecipeSummary.setHTML(it.summary.summary)

            fillAnalyzedInstructions(it.analyzedInstructions)
        })

        viewModel.fabState.observe(this, Observer<Boolean> { show ->
            if (show) {
                recipeInfoLayoutFAB.animate().translationY(0F).alpha(1F)
                    .setInterpolator(DecelerateInterpolator(2F)).start()
            } else {
                recipeInfoLayoutFAB.animate().translationY(recipeInfoLayoutFAB.measuredHeight.toFloat() + recipeInfoLayoutFAB.marginBottom).alpha(
                    0F
                ).setInterpolator(AccelerateInterpolator(2F)).start()
            }
        })

        viewModel.progressState.observe(viewLifecycleOwner, Observer { progressIsVisible->
            if(progressIsVisible){
                recipeInfoLayoutProgressBar.visibility = View.VISIBLE
                recipeInfoLayoutContentView.visibility = View.GONE
            }else{
                recipeInfoLayoutProgressBar.visibility = View.GONE
                recipeInfoLayoutContentView.visibility = View.VISIBLE
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
                        viewModel.getRecipeInfo()
                    }.setActionTextColor(ContextCompat.getColor(view.context, R.color.colorAccent))

                snackbar?.show()
            }else{
                snackbar?.dismiss()
            }
        })

        recipeInfoLayoutFAB.setOnClickListener {
            recipeInfoLayoutFAB.isSelected = !recipeInfoLayoutFAB.isSelected

            if(!viewModel.isRecipeSaved) {
                viewModel.saveRecipe()
                Snackbar.make(
                    view,
                    resources.getString(R.string.recipe_was_saved),
                    Snackbar.LENGTH_SHORT
                ).show()
            }else{
                viewModel.deleteRecipe()
                Snackbar.make(
                    view,
                    resources.getString(R.string.recipe_was_deleted),
                    Snackbar.LENGTH_SHORT
                ).show()
                }
        }

        recipeInfoLayoutScrollView.setOnScrollChangeFabListener(this)

        recipeInfoLayoutScrollView.viewTreeObserver.addOnScrollChangedListener(this)
    }

    override fun onScrollChanged() {
        val textScale = (toolbarHeight/
                ((if(recipeInfoLayoutScrollView.scrollY < toolbarHeight) recipeInfoLayoutScrollView.scrollY else toolbarHeight)/4+toolbarHeight).toFloat())

        recipeInfoLayoutRecipeTitle.textSize = 24*textScale
        recipeInfoLayoutRecipeTitle.elevation = (1-textScale)*50
    }

    override fun show() {
        viewModel.showFAB()
    }

    override fun hide() {
        viewModel.hideFAB()
    }

    override fun onScroll(scrollY: Int) {}

    override fun onDestroyView() {
        snackbar?.dismiss()
        recipeInfoLayoutScrollView.viewTreeObserver.removeOnScrollChangedListener(this)
        super.onDestroyView()
    }

    private fun TextView.setHTML(html: String) {
        val sequence: CharSequence = Html.fromHtml(html)
        val strBuilder = SpannableStringBuilder(sequence)
        val urls =
            strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
        for (span in urls) {
            makeLinkClickable(strBuilder, span)
        }
        text = strBuilder
        movementMethod = LinkMovementMethod.getInstance()
    }

    private fun fillAnalyzedInstructions(analyzedInstructionsEntity: RecipeAnalyzedInstructionsUiModel){
        analyzedInstructionsEntity.analyzedInstructions.forEach { instruction->
            val instructionView = LinearLayout(recipeInfoLayoutRecipeAnalyzedInstructions.context).apply {
                orientation = VERTICAL
            }
            instruction.steps.forEach { step->
                val stepView = recipeInfoLayoutRecipeAnalyzedInstructions.makeView(R.layout.analyzed_instructions_step_layout)
                stepView.analyzedInstructionsStepLayoutStepText.text = step.text
                stepView.analyzedInstructionsStepLayoutStepNumber.text = step.number

                if(step.ingredients.isEmpty() || step.equipment.isEmpty()){
                    stepView.analyzedInstructionsStepLayoutFlexboxesDivider.visibility = View.GONE

                    if(step.ingredients.isEmpty())
                        stepView.analyzedInstructionsStepLayoutIngredientsFlexbox.visibility = View.GONE
                    else
                        stepView.analyzedInstructionsStepLayoutEquipmentFlexbox.visibility = View.GONE
                }

                step.ingredients.forEach { ingredient->
                    val ingredientView = recipeInfoLayoutRecipeAnalyzedInstructions.makeView(R.layout.analyzed_instructions_step_flexbox_item_layout)

                    Glide
                        .with(this)
                        .load(ingredient.imageLink)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ingredientView.analyzedInstructionsStepFlexboxItemLayoutImage)

                    ingredientView.analyzedInstructionsStepFlexboxItemLayoutText.text = ingredient.ingredientName

                    stepView.analyzedInstructionsStepLayoutIngredientsFlexbox.addView(ingredientView)
                }
                step.equipment.forEach { equipment->
                    val equipmentView = recipeInfoLayoutRecipeAnalyzedInstructions.makeView(R.layout.analyzed_instructions_step_flexbox_item_layout)

                    Glide
                        .with(this)
                        .load(equipment.imageLink)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(equipmentView.analyzedInstructionsStepFlexboxItemLayoutImage)

                    equipmentView.analyzedInstructionsStepFlexboxItemLayoutText.text = equipment.equipmentName

                    stepView.analyzedInstructionsStepLayoutEquipmentFlexbox.addView(equipmentView)
                }
                instructionView.addView(stepView)
            }
            recipeInfoLayoutRecipeAnalyzedInstructions.addView(instructionView)
        }
    }

    private fun makeLinkClickable(
        strBuilder: SpannableStringBuilder,
        span: URLSpan
    ) {
        val start = strBuilder.getSpanStart(span)
        val end = strBuilder.getSpanEnd(span)
        val flags = strBuilder.getSpanFlags(span)
        val clickable: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                (activity as AppCompatActivity).replaceFragment(getInstance(span.url.split('-').last().toLong(), strBuilder.toString().substring(start, end)), R.id.frameLayout)
            }
        }
        val underline: UnderlineSpan = object : UnderlineSpan() {
            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
            }
        }
        strBuilder.setSpan(clickable, start, end, flags)
        strBuilder.setSpan(underline, start, end, flags)
        strBuilder.removeSpan(span)
    }

}