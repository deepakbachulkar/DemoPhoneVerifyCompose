package com.demo.lloydstest.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test


class UtilsTest{

    @Test
    fun emptyCountyCodeReturnTrue(){
        val result = Utils.validationInput("", "9773523570")
        assertThat(result).isFalse()
    }

    @Test
    fun emptyPhoneNumberReturnTrue(){
        val result = Utils.validationInput("+91", "")
        assertThat(result).isFalse()
    }

    @Test
    fun validCountryCodeAndPhoneNumberReturnTrue(){
        val result = Utils.validationInput("+91", "9773523570")
        assertThat(result).isTrue()
    }
}