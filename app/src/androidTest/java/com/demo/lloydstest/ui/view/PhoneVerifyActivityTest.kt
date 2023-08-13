package com.demo.lloydstest.ui.view


import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.demo.lloydstest.ui.utils.Tag.BUTTON_VERIFY
import com.demo.lloydstest.ui.utils.Tag.COUNTRY_CODE
import com.demo.lloydstest.ui.utils.Tag.DROP_DOWN_SELECT
import com.demo.lloydstest.ui.utils.Tag.PHONE_NUMBER
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Timer
import kotlin.concurrent.schedule

internal class PhoneVerifyActivityTest()  {

    @get:Rule()
    val composeRule = createAndroidComposeRule<PhoneVerifyActivity>()

    @Before
    fun setup(){
    }

    /**
     * Test case 1:
     * if we click on verify without selecting country code and phone number
     * then It gives error message
     */
    @Test
    fun showErrorMessageDirectClickVerifyButton() {
        composeRule.onNodeWithTag(BUTTON_VERIFY).performClick()
    }

    /**
     * Test case 2:
     * if we select only country code
     * then It gives error message
     */
    @Test
    fun selectCountryCodeAndVerifyShowError(){
        //For api call purpose add delay
        asyncTimer(4000)
        composeRule.onNodeWithTag(COUNTRY_CODE).performClick()
        composeRule.onNodeWithTag(DROP_DOWN_SELECT).performClick()
        composeRule.onNodeWithTag(BUTTON_VERIFY).performClick()
    }

    /**
     *  Test case 3:
     * if we enter only phone number
     * then It gives error message
     */
    @Test
    fun enterPhoneNumberAndVerifyShowError(){
        val text = "9773523570"
        composeRule.onNodeWithTag(PHONE_NUMBER).performTextInput(text)
        composeRule.onNodeWithTag(BUTTON_VERIFY).performClick()
    }

    /**
     * if we select country code and enter phone number
     * then Call the API
     */
    @Test
    fun selectCountryCodeAndEnterPhoneNumberVerifyApiCall(){
        //For api call purpose add delay
        asyncTimer(4000)
        composeRule.onNodeWithTag(COUNTRY_CODE).performClick()
        composeRule.onNodeWithTag(DROP_DOWN_SELECT).performClick()
        val text = "9773523570"
        composeRule.onNodeWithTag(PHONE_NUMBER).performTextInput(text)
        composeRule.onNodeWithTag(BUTTON_VERIFY).performClick()
    }

    /**
     * if we select country code and enter valid phone number
     * then number is getting verified and shows message 'Phone number is valid'
     * as well as shows phone number details.
     */
    @Test
    fun selectCountryCodeAndEnterPhoneNumberVerifyValid(){
        //For api call purpose add delay
        asyncTimer(4000)
        composeRule.onNodeWithTag(COUNTRY_CODE).performClick()
        composeRule.onNodeWithTag(DROP_DOWN_SELECT).performClick()
        val text = "2025550186"
        composeRule.onNodeWithTag(PHONE_NUMBER).performTextInput(text)
        composeRule.onNodeWithTag(BUTTON_VERIFY).performClick()
        composeRule.waitUntil {
            composeRule
                .onAllNodesWithText("Phone number is valid")
                .fetchSemanticsNodes().size == 1
        }
    }

    /**
     * if we select country code and enter wong phone number
     * then number is getting verified and shows message 'Phone number is not valid'
     */
    @Test
    fun selectCountryCodeAndEnterPhoneNumberVerifyNotValid(){
        //For api call purpose add delay
        asyncTimer(4000)
        composeRule.onNodeWithTag(COUNTRY_CODE).performClick()
        composeRule.onNodeWithTag(DROP_DOWN_SELECT).performClick()
        val text = "202"
        composeRule.onNodeWithTag(PHONE_NUMBER).performTextInput(text)
        composeRule.onNodeWithTag(BUTTON_VERIFY).performClick()
        composeRule.waitUntil {
            composeRule
                .onAllNodesWithText("Phone number is not valid")
                .fetchSemanticsNodes().size == 1
        }
    }

    private fun asyncTimer (delay: Long = 1000) {
        AsyncTimer.start (delay)
        composeRule.waitUntil (
            condition = {AsyncTimer.expired},
            timeoutMillis = delay + 1000
        )
    }

    object AsyncTimer {
        var expired = false
        fun start(delay: Long = 1000){
            expired = false
            Timer().schedule(delay) {
                expired = true
            }
        }
    }
}