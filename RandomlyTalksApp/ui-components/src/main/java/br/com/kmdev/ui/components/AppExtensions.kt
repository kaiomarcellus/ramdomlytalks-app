package br.com.kmdev.ui.components

import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

fun Context.dpToPx(dp: Float): Float {
    return dp * getPixelScaleFactor(this)
}

private fun getPixelScaleFactor(context: Context): Float {
    val displayMetrics = context.resources.displayMetrics
    return displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT
}

fun View.setVisible(visibleCondition: Boolean, toInvisible: Boolean = false) {
    visibility = visibleCondition.fetchVisibility(toInvisible = toInvisible)
}

fun Boolean.fetchVisibility(toInvisible: Boolean = false): Int {
    return if (this) View.VISIBLE else if (toInvisible) View.INVISIBLE else View.GONE
}

fun View.hideKeyboard() {
    if (context == null) return
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.toast(text: String) {
    Toast.makeText(this.context, text, Toast.LENGTH_SHORT).show()
}