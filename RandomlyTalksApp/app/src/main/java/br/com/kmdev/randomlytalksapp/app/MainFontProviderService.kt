package br.com.kmdev.randomlytalksapp.app

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import br.com.kmdev.randomlytalksapp.R
import br.com.kmdev.ui.components.FontProvider

class MainFontProviderService(private val context: Context) : FontProvider {

    override fun getRegularFont(): Typeface? {
        return ResourcesCompat.getFont(context, R.font.manrope_regular)
    }

    override fun getMediumFont(): Typeface? {
        return ResourcesCompat.getFont(context, R.font.manrope_medium)
    }

    override fun getBoldFont(): Typeface? {
        return ResourcesCompat.getFont(context, R.font.manrope_bold)
    }

    override fun getExtraBoldFont(): Typeface? {
        return ResourcesCompat.getFont(context, R.font.manrope_extra_bold)
    }

}