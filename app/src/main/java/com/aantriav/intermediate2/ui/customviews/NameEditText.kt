package com.aantriav.intermediate2.ui.customviews

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewParent
import com.aantriav.intermediate2.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class NameEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputEditText(context, attrs, defStyleAttr) {

    private var isInitialized = false

    init {
        setupView()
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val parentTextInputLayout = findParentTextInputLayout()
                if (!isInitialized) {
                    return
                }

                if (s.isNullOrEmpty()) {
                    parentTextInputLayout?.error = context.getString(R.string.error_empty_name)
                } else {
                    parentTextInputLayout?.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupView() {
        isFocusable = true
        isFocusableInTouchMode = true
        isClickable = true
        isEnabled = true
        inputType = InputType.TYPE_CLASS_TEXT
        gravity = Gravity.CENTER_VERTICAL
    }

    private fun findParentTextInputLayout(): TextInputLayout? {
        var parentView: ViewParent? = parent
        while (parentView != null) {
            if (parentView is TextInputLayout) {
                return parentView
            }
            parentView = (parentView as? View)?.parent
        }
        return null
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        isInitialized = true
    }
}
