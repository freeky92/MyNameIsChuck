package com.asurspace.mynameischuck.contract

import androidx.fragment.app.Fragment

fun Fragment.navigator(): Navigator{
    return requireActivity() as Navigator
}

interface Navigator {

    fun launchNext()
    fun generateUuid(): String
    fun update()

}