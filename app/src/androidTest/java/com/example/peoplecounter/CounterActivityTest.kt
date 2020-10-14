package com.example.peoplecounter

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.peoplecounter.CounterActivity.Companion.MAX_THRESHOLD
import com.example.peoplecounter.CounterActivity.Companion.TOTAL_COUNT_KEY
import com.example.peoplecounter.CounterActivity.Companion.TOTAL_PEOPLE_KEY
import com.example.peoplecounter.util.OrientationChangeAction.Companion.orientationLandscape
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CounterActivityTest {

    lateinit var scenario: ActivityScenario<CounterActivity>
    private val sharedPref = InstrumentationRegistry.getInstrumentation().targetContext
        .getSharedPreferences("CounterActivity", Context.MODE_PRIVATE)

    @Test
    fun incrementAndDecrementCounters_sameActivity(){
        sharedPref.edit().putInt(TOTAL_COUNT_KEY, MAX_THRESHOLD).putInt(TOTAL_PEOPLE_KEY, MAX_THRESHOLD).commit()
        scenario = launchActivity()
        onView(withId(R.id.btn_add)).perform(click())
        onView(withId(R.id.tv_people_counter)).check(matches(withText("${MAX_THRESHOLD + 1} People")))
        //over max capacity, so text should turn red
        onView(withId(R.id.tv_people_counter)).check(matches(hasTextColor(R.color.red)))
        onView(withId(R.id.tv_total_counter))
            .check(matches(withText("Total Count: ${MAX_THRESHOLD + 1}")))

        onView(withId(R.id.btn_reduce)).perform(click())
        onView(withId(R.id.tv_people_counter)).check(matches(withText("$MAX_THRESHOLD People")))
        //changes the color black to blue because the total people goes lower than 16
        onView(withId(R.id.tv_people_counter)).check(matches(hasTextColor(R.color.blue)))
        //total count never reduces after pressing the - button
        onView(withId(R.id.tv_total_counter)).check(matches(withText("Total Count: ${MAX_THRESHOLD + 1}")))
        scenario.close()
    }

    @Test
    fun resetCounters_sameActivity(){
        sharedPref.edit().putInt(TOTAL_COUNT_KEY, MAX_THRESHOLD).putInt(TOTAL_PEOPLE_KEY, MAX_THRESHOLD).commit()
        scenario = launchActivity()
        onView(withId(R.id.btn_reset)).perform(click()) //should reset the counts back to 0
        onView(withId(R.id.tv_people_counter)).check(matches(withText("0 People")))
        onView(withId(R.id.tv_total_counter)).check(matches(withText("Total Count: 0")))
        scenario.close()
    }

    @Test
    fun hasCorrectState_afterRotation(){
        //just in case we need another test that is alphabetically before this we reset shared prefs
        //both counts will be 0.
        sharedPref.edit().clear().commit()
        scenario = launchActivity()
        onView(withId(R.id.btn_add)).perform(click())
        onView(isRoot()).perform(orientationLandscape())
        onView(withId(R.id.tv_total_counter)).check(matches(withText("Total Count: 1")))
        onView(withId(R.id.tv_people_counter)).check(matches(withText("1 People")))
        scenario.close()
    }
}