package com.example.herculeswallet

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.herculeswallet.view.MainActivity

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.runners.MethodSorters

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class LoginInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.herculeswallet", appContext.packageName)
    }

    @Rule
    @JvmField var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    fun A_verifyLayoutLabelContent(){
        onView(withId(R.id.button_accedi))
            .check(matches(withText("Accedi")))
        onView(withId(R.id.button_registrati))
            .check(matches(withText("Registrati")))
    }

    @Test
    fun C_checkLogin(){
        onView(withId(R.id.email))
            .perform(typeText("marco.rossi@gmail.com"))
        closeSoftKeyboard()
        onView(withId(R.id.password)).perform(replaceText("Mattia98"))
        closeSoftKeyboard()
        onView(withId(R.id.button_accedi))
            .perform(click())
    }

    @Test
    fun B_dialogCheck(){
        onView(withId(R.id.button_accedi))
            .perform(click())
        onView(withText("I campi non sono stati compilati!")).check(matches(isDisplayed()))
    }

}