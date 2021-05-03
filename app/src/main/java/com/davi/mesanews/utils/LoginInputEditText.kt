package com.davi.mesanews.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginInputEditText : TextInputEditText {

    constructor(context: Context) : super(context) {
        start()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        start()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    ) {
        start()
    }

    private fun start() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    val inputLayout: TextInputLayout = this@LoginInputEditText.parent.parent as TextInputLayout
                    inputLayout.error = null
                    inputLayout.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
}