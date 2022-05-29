package br.com.kmdev.ui.components

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.widget.doAfterTextChanged
import br.com.kmdev.ui.components.databinding.InputComponentViewBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.math.roundToInt

class InputComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : LinearLayoutCompat(context, attrs, defStyle), KoinComponent {

    private val fontProvider: FontProvider by inject()
    private var binding: InputComponentViewBinding? = null
    private var inputLayout: TextInputLayout? = null
    private var inputEditText: TextInputEditText? = null
    private var inputProgress: ProgressBar? = null
    private var inputError: TextView? = null

    var inputChangeCallback: ((content: String?) -> Unit)? = null
    var imeOptionsCallback: ((content: String?) -> Unit)? = null

    var text: String? = null
        set(value) {
            field = value
            value?.let { inputEditText?.setText(value) }
        }
    var enabled: Boolean? = null
        set(value) {
            field = value
            value?.let {
                inputLayout?.isEnabled = value
                inputEditText?.isEnabled = value
            }
        }
    var drawableEnd: Int? = null
        set(value) {
            field = value
            value?.let {
                inputEditText?.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0, 0, it, 0
                )
            }
        }
    var loading: Boolean? = null
        set(value) {
            field = value
            value?.let {
                inputProgress?.setVisible(it)
            }
        }

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = InputComponentViewBinding.inflate(inflater, this, true)
        inputLayout = binding?.inputLayout
        inputEditText = binding?.inputContent
        inputError = binding?.inputErrorMessage
        inputProgress = binding?.inputProgress
        orientation = VERTICAL
        context.obtainStyledAttributes(attrs, R.styleable.InputComponent, defStyle, 0).apply {

            val inputHint = getString(R.styleable.InputComponent_android_hint) ?: ""
            val inputText = getString(R.styleable.InputComponent_android_text) ?: ""
            val inputDrawableEnd = getResourceId(R.styleable.InputComponent_android_drawableEnd, 0)
            val inputMultiline = getBoolean(R.styleable.InputComponent_inputMultiline, false)
            val inputPassword = getBoolean(R.styleable.InputComponent_inputPassword, false)
            val inputAction =
                getInt(R.styleable.InputComponent_android_imeOptions, EditorInfo.IME_ACTION_NEXT)
            val inputType = getInt(
                R.styleable.InputComponent_android_inputType,
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            )
            val inputGravity = getInt(
                R.styleable.InputComponent_android_gravity,
                Gravity.START or Gravity.CENTER_VERTICAL
            )

            if (inputMultiline) {
                binding?.inputContainer?.layoutParams?.apply {
                    width = FrameLayout.LayoutParams.MATCH_PARENT
                    height = context.dpToPx(200F).roundToInt()
                }
            }

            val typeface = when {
                getBoolean(R.styleable.InputComponent_inputFontMedium, false) -> {
                    fontProvider.getMediumFont()
                }
                getBoolean(R.styleable.InputComponent_inputFontBold, false) -> {
                    fontProvider.getBoldFont()
                }
                getBoolean(R.styleable.InputComponent_inputFontExtraBold, false) -> {
                    fontProvider.getExtraBoldFont()
                }
                else -> fontProvider.getRegularFont()
            }

            if (inputDrawableEnd != 0) {
                inputEditText?.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0,
                    0, inputDrawableEnd, 0
                )
            }

            if (inputHint.isNotEmpty()) inputEditText?.hint = inputHint
            if (inputText.isNotEmpty()) inputEditText?.setText(inputText)
            if (inputPassword) {
                inputLayout?.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                inputLayout?.setEndIconTintList(ColorStateList.valueOf(Color.BLACK))
            }
            inputEditText?.gravity = inputGravity
            inputEditText?.typeface = typeface
            inputEditText?.inputType = inputType
            inputEditText?.imeOptions = inputAction
            inputEditText?.setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE,
                    EditorInfo.IME_ACTION_NEXT,
                    EditorInfo.IME_ACTION_SEARCH,
                    EditorInfo.IME_ACTION_SEND,
                    -> {
                        imeOptionsCallback?.invoke(getContentText())
                    }
                }
                return@setOnEditorActionListener false
            }
            inputEditText?.doAfterTextChanged {
                setError(null)
                inputChangeCallback?.invoke(getContentText())
            }

            recycle()
        }
    }

    fun getEditText() = inputEditText

    fun getContentText() = inputEditText?.text.toString().trim()

    fun focus() {
        inputEditText?.requestFocus()
    }

    fun setError(errorMessageRes: Int) {
        val errorMessage = context.getString(errorMessageRes)
        inputLayout?.error = errorMessage
        inputError?.text = errorMessage
        inputError?.setVisible(errorMessage.isNotEmpty())
        drawableEnd = null
    }

    fun setError(errorMessage: String?) {
        inputLayout?.error = errorMessage
        inputError?.text = errorMessage
        inputError?.setVisible(!errorMessage.isNullOrEmpty())
        drawableEnd = null
    }

    fun addWatcher(watcher: TextWatcher) {
        inputEditText?.addTextChangedListener(watcher)
    }

    fun removeWatcher(watcher: TextWatcher) {
        inputEditText?.removeTextChangedListener(watcher)
    }

    fun clear() {
        inputEditText?.text?.clear()
    }

}