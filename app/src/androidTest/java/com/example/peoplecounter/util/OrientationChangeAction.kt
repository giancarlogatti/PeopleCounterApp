package com.example.peoplecounter.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import org.hamcrest.Matcher

/*
    Wanted to figure out how to test the state after rotation and found this
    https://gist.github.com/nbarraille/03e8910dc1d415ed9740, worked like a charm!
 */
class OrientationChangeAction(private val orientation: Int): ViewAction {

    companion object {
        fun orientationLandscape(): ViewAction = OrientationChangeAction(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
    }

    override fun getDescription(): String = "change orientation to $orientation"

    override fun getConstraints(): Matcher<View> = isRoot()

    override fun perform(uiController: UiController, view: View) {
        uiController.loopMainThreadUntilIdle()
        var activity = getActivity(view.context)
        if (activity == null && view is ViewGroup) {
            val c = view.childCount
            var i = 0
            while (i < c && activity == null) {
                activity = getActivity(view.getChildAt(i).context)
                ++i
            }
        }
        activity!!.requestedOrientation = orientation
    }

    private fun getActivity(context: Context): Activity? {
        var context = context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }
}