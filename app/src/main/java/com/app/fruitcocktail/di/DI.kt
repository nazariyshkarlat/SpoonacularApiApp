package com.app.fruitcocktail.di

import android.app.Application
import com.app.fruitcocktail.di.entities_di.RecipesAnalyzedInstructionsModule
import com.app.fruitcocktail.di.entities_di.RecipesSummaryModule
import com.app.fruitcocktail.di.entities_di.RecipesModule

object DI {


    sealed class Config {
        object RELEASE : Config()
        object TEST : Config()
    }

    fun initialize(app: Application, configuration: Config = Config.RELEASE) {
        NetworkModule.initialize(app)
        DatabaseModule.initialize(app)
        RecipesSummaryModule.initialize(configuration)
        RecipesAnalyzedInstructionsModule.initialize(configuration)
        RecipesModule.initialize(configuration)
    }

}