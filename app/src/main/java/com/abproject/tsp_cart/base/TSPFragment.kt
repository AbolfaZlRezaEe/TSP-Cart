package com.abproject.tsp_cart.base

import android.content.Context
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment

/**
 * Created by Abolfazl on 5/22/21
 */
abstract class TSPFragment : Fragment(), TSPInterface {
    override val rootView: CoordinatorLayout?
        get() = view as CoordinatorLayout?
    override val viewContext: Context?
        get() = context

}