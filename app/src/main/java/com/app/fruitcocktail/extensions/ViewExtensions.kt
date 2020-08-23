package com.app.fruitcocktail.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.widget.NestedScrollView
import com.app.fruitcocktail.presentation.listeners.FabScrollImpl
import com.app.fruitcocktail.presentation.listeners.FabScrollListener

fun ViewGroup.makeView(@LayoutRes layoutResId: Int): View =
    LayoutInflater.from(this.context).inflate(layoutResId, this, false)

fun NestedScrollView.setOnScrollChangeFabListener(fabScrollListener: FabScrollListener) {
    this.setOnScrollChangeListener(FabScrollImpl(fabScrollListener))
}