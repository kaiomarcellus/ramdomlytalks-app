package br.com.kmdev.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@SuppressLint("CustomViewStyleable")
class RadioButtonComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.radioButtonStyle
) : androidx.appcompat.widget.AppCompatRadioButton(context, attrs, defStyleAttr), KoinComponent {

    private val fontProvider: FontProvider by inject()

    init {
        context.obtainStyledAttributes(attrs, R.styleable.RadioButtonComponent, defStyleAttr, 0)
            .apply {
                typeface = when {
                    getBoolean(R.styleable.RadioButtonComponent_radioButtonFontMedium, false) -> {
                        fontProvider.getMediumFont()
                    }
                    getBoolean(R.styleable.RadioButtonComponent_radioButtonFontBold, false) -> {
                        fontProvider.getBoldFont()
                    }
                    getBoolean(
                        R.styleable.RadioButtonComponent_radioButtonFontExtraBold,
                        false
                    ) -> {
                        fontProvider.getExtraBoldFont()
                    }
                    else -> fontProvider.getRegularFont()
                }

                recycle()
            }
    }
}