package com.app.fruitcocktail.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.fruitcocktail.R
import com.app.fruitcocktail.extensions.addFragment
import com.app.fruitcocktail.presentation.fragments.reciepes_list.RecipesListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.frame_layout)

        if(savedInstanceState == null)
            addFragment(RecipesListFragment(), R.id.frameLayout)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}