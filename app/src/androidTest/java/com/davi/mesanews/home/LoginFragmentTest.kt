package com.davi.mesanews.home

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.davi.mesanews.R
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(HomeActivity::class.java)

    @Test
    fun test_isVisible() {
        onView(withId(R.id.login_logo)).check(matches(isDisplayed()))
        onView(withId(R.id.login_loading_view)).check(matches(not(isDisplayed())))

        onView(withId(R.id.login_email_field)).perform(typeText("john@doe.com"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.login_password_field)).perform(typeText("123456"))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.login_button)).perform(click())

        onView(withId(R.id.login_loading_view)).check(matches(isDisplayed()))
    }
}