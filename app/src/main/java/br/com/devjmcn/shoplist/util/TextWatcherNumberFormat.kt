package br.com.devjmcn.shoplist.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import br.com.devjmcn.shoplist.R
import br.com.devjmcn.shoplist.util.extensions.toBigDecimalFormat


class TextWatcherNumberFormat(private val editText: EditText) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(editable: Editable) {
        if (editText.text.isEmpty()) return
        try {
            editText.removeTextChangedListener(this)

            val parsedText = editText.text.toString().toBigDecimalFormat()

            val formatted: String = numberFormat.format(parsedText)

            editText.setText(formatted)
            editText.setSelection(formatted.length)
            editText.addTextChangedListener(this)
        } catch (e: Exception) {
            Toast.makeText(
                editText.context,
                editText.context.getString(R.string.str_maximum_allowed_value), Toast.LENGTH_SHORT
            ).show()
        }
    }
}