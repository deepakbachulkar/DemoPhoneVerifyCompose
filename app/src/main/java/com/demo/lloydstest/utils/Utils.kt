package com.demo.lloydstest.utils

object Utils{

    fun validationInput(code:String, phone:String):Boolean{
        return if(code.isEmpty())
            false
        else !phone.isEmpty()
    }
}