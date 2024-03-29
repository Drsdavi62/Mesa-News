package com.davi.mesanews.utils

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginInputEditText : TextInputEditText {

    lateinit var inputLayout: TextInputLayout

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(
        context,
        attributeSet,
        defStyle
    )

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        inputLayout = (this@LoginInputEditText.parent.parent as TextInputLayout?)!!
        start()
    }

    private fun start() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    inputLayout?.error = null
                    inputLayout?.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    fun setError(message: String) {
        inputLayout?.error = message
    }
}