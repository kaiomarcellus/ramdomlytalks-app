package br.com.kmdev.ui.components

import android.graphics.Typeface

interface FontProvider {

    fun getRegularFont(): Typeface?
    fun getMediumFont(): Typeface?
    fun getBoldFont(): Typeface?
    fun getExtraBoldFont(): Typeface?

}