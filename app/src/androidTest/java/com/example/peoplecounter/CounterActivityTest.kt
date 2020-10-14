package com.example.peoplecounter

import android.content.Intent
import android.os.Bundle
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.peoplecounter.CounterActivity.Companion.PEOPLE_COUNT_THRESHOLD
import com.example.peoplecounter.util.OrientationChangeAction.Companion.orientationLandscape
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class CounterActivityTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<CounterActivity> = ActivityScenarioRule(
        //this intent will have the keys which will be placed into the SaveStateHandle when this activity is
        //launched for test
        Intent(ApplicationProvider.getApplicationContext(), CounterActivity::class.java).apply{
            val bundle = Bundle()
            bundle.putInt(CounterViewModel.TOTAL_COUNT_KEY, PEOPLE_COUNT_THRESHOLD)
            bundle.putInt(CounterViewModel.TOTAL_PEOPLE_KEY, PEOPLE_COUNT_THRESHOLD)
            putExtras(bundle)
        }
    )

    @Test
    fun incrementAndDecrementCounters_sameActivity(){
        onView(withId(R.id.btn_add)).perform(click())
        onView(withId(R.id.tv_people_counter)).check(matches(withText("16 People")))
        //also make sure that tv_people_counter text color changes to red when its > 15
        onView(withId(R.id.tv_people_counter)).check(matches(hasTextColor(R.color.red)))
        onView(withId(R.id.tv_total_counter)).check(matches(withText("Total Count: 16")))

        onView(withId(R.id.btn_reduce)).perform(click())
        onView(withId(R.id.tv_people_counter)).check(matches(withText("15 People")))
        //changes the color black to blue if goes lower than 16
        onView(withId(R.id.tv_people_counter)).check(matches(hasTextColor(R.color.blue)))
        //total count never reduces even after pressing the - button (only goes to 0 if count is reset)
        onView(withId(R.id.tv_total_counter)).check(matches(withText("Total Count: 16")))
    }

    @Test
    fun resetCounters_sameActivity(){
        onView(withId(R.id.btn_reset)).perform(click())
        onView(withId(R.id.tv_people_counter)).check(matches(withText("0 People")))
        onView(withId(R.id.tv_total_counter)).check(matches(withText("Total Count: 0")))
    }

    @Test
    fun hasCorrectState_afterRotation(){
        onView(withId(R.id.btn_reduce)).perform(click())
        onView(isRoot()).perform(orientationLandscape())
        onView(withId(R.id.tv_total_counter)).check(matches(withText("Total Count: 15")))
        onView(withId(R.id.tv_people_counter)).check(matches(withText("14 People")))
    }
}