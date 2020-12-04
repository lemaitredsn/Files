package ru.lemaitre.filelesson.extensions

import android.content.Context
import android.widget.Toast

infix fun<T: Context> T.showToast(text:String) {
    return Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

