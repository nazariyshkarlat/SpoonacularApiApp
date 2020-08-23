package com.app.fruitcocktail

import android.app.Application
import com.app.fruitcocktail.di.DI

class App : Application(){

    override fun onCreate() {
        super.onCreate()
        DI.initialize(this)
    }

}