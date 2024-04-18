package com.hisaki.stockwiseapp

import android.util.Patterns
import android.text.TextUtils

object Validator {
    fun isTextNotEmpty(text: String?):Boolean{
        return !TextUtils.isEmpty(text)
    }

    fun isValidEmail(text: String): Boolean{
        return if (TextUtils.isEmpty(text))false
        else Patterns.EMAIL_ADDRESS.matcher(text).matches()
    }
}