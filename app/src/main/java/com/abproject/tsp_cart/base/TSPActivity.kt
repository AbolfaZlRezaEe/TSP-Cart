package com.abproject.tsp_cart.base

import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.forEach


/**
 * Created by Abolfazl on 5/22/21
 */
abstract class TSPActivity : AppCompatActivity(), TSPInterface {
    override val rootView: CoordinatorLayout?
        get() {
            val viewGroup = window.decorView.findViewById(android.R.id.content) as ViewGroup
            if (viewGroup !is CoordinatorLayout) {
                viewGroup.forEach {
                    if (it is CoordinatorLayout)
                        return it
                }
                throw IllegalStateException("rootView must be instance of CoordinatorLayout!")
            } else
                return viewGroup
        }
    override val viewContext: Context?
        get() = this
}