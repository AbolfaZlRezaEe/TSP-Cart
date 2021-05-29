package com.abproject.tsp_cart.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.abproject.tsp_cart.R
import com.google.android.material.snackbar.Snackbar

/**
 * Created by Abolfazl on 5/22/21
 */
interface TSPInterface {
    val rootView: CoordinatorLayout?
    val viewContext: Context?

    /**
     * showProgressBar Function use in views (Activity or Fragment)
     * and that help us for showing progressbar in view.
     * Note: this function only works on views that have Coordinator Layout!
     */
    fun showProgressBar(isShow: Boolean) {
        rootView?.let {
            viewContext?.let { context ->
                var loadingState = it.findViewById<View>(R.id.loadingState)
                if (loadingState == null && isShow) {
                    loadingState = LayoutInflater.from(context)
                        .inflate(R.layout.state_loading, rootView, false)
                    it.addView(loadingState)
                }
                loadingState?.visibility = if (isShow) View.VISIBLE else View.GONE
            }
        }
    }

    /**
     * showSnackBar is a custom Function I made for views
     * this function working in Activities and Fragments.
     * Note: this Function only works on views that have Coordinator Layout!
     */
    fun showSnackBar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
        rootView?.let {
            Snackbar.make(it, message, duration).show()
        }
    }

}
