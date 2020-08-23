package com.app.fruitcocktail.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

fun AppCompatActivity.addFragment(fragment: Fragment, container: Int) {
    supportFragmentManager.beginTransaction()
        .add(container, fragment, fragment::class.java.name)
        .commit()
}

fun AppCompatActivity.replaceFragment(
    fragment: Fragment,
    container: Int,
    startAnim: Int = 0,
    endAnim: Int = 0
) {
    supportFragmentManager.beginTransaction()
        .setCustomAnimations(startAnim, endAnim, 0, 0)
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        .replace(container, fragment)
        .addToBackStack(fragment.toString())
        .commit()
}
