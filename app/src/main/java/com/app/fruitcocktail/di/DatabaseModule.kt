package com.app.fruitcocktail.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.app.data.database.AppDatabase

object DatabaseModule {

    lateinit var appDatabase: AppDatabase

    fun initialize(app: Application) {
        appDatabase = Room.databaseBuilder(
            app.applicationContext,
            AppDatabase::class.java,
            AppDatabase.DB_NAME
        ).build()
    }
}