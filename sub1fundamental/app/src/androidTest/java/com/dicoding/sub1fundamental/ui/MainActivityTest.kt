package com.dicoding.sub1fundamental.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.dicoding.sub1fundamental.R



@RunWith(AndroidJUnit4::class)
class MainActivityUITest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(MainActivity::class.java)


    @Test
    fun testOpenMenu() {
        onView(withId(R.id.btn_menu)).perform(click())
        Thread.sleep(3000)
//        memeriksa apakah menu item adalah menu Theme Setting
        onView(withText("Theme Setting")).check(matches(isDisplayed()))
    }

    @Test
    fun testOpenFavoriteActivity() {
        // Klik tombol ke Favorit
        onView(withId(R.id.btn_favorite)).perform(click())
        Thread.sleep(1000)

        //        memeriksa apakah di dalam activity favorite ada text User Favorite Kosong!!
        onView(withId(R.id.tvFavorite)).check(matches(withText("User Favorite Kosong!!")))
    }

}




