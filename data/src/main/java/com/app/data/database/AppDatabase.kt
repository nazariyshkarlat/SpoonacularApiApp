package com.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.data.database.recipe_analyzed_instructions.*
import com.app.data.database.recipe_summary.RecipesSummaryDao
import com.app.data.database.recipes_list.RecipesListDao
import com.app.data.database.recipes_list.TimestampConverter
import com.app.data.entity.RecipeAnalyzedInstructionsEntity
import com.app.data.entity.RecipeEntity
import com.app.data.entity.RecipeSummaryEntity

@TypeConverters(RecipeAnalyzedInstructionsTypeConverter::class, StepsTypeConverter::class, IngredientsTypeConverter::class, EquipmentTypeConverter::class, TimestampConverter::class)
@Database(
    entities = [RecipeEntity::class, RecipeSummaryEntity::class, RecipeAnalyzedInstructionsEntity::class],
    version = AppDatabase.DB_VERSION
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getRecipesListDao(): RecipesListDao

    abstract fun getRecipeSummaryDao(): RecipesSummaryDao

    abstract fun getRecipeAnalyzedInstructionsDao(): RecipesAnalyzedInstructionsDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "app_database"
    }
}