package com.app.domain.entity

import java.util.*

data class RecipeData(val recipeId: Long,
                      val title: String,
                      val imageLink: String,
                      var date: Date? = null)