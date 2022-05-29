package br.com.kmdev.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@SuppressLint("CustomViewStyleable")
class TextViewComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr), KoinComponent {

    private val fontProvider: FontProvider by inject()

    init {
        context.obtainStyledAttributes(attrs, R.styleable.TextViewComponent, defStyleAttr, 0)
            .apply {
                typeface = when {
                    getBoolean(R.styleable.TextViewComponent_textViewFontMedium, false) -> {
                        fontProvider.getMediumFont()
                    }
                    getBoolean(R.styleable.TextViewComponent_textViewFontBold, false) -> {
                        fontProvider.getBoldFont()
                    }
                    getBoolean(R.styleable.TextViewComponent_textViewFontExtraBold, false) -> {
                        fontProvider.getExtraBoldFont()
                    }
                    else -> fontProvider.getRegularFont()
                }

                recycle()
            }
    }
}