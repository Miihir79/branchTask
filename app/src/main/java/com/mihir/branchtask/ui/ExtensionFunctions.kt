package com.mihir.branchtask.ui

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun View.showToast(message:String){
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun Fragment.showToast(message: String) = requireView().showToast(message)

fun String.formatDate(): String {
    val simpleDateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    val timeStamp: Date = simpleDateFormat.parse(this)
    val formatter: DateFormat = SimpleDateFormat("dd/MM/yy" + "  HH:mm aaa")
    return formatter.format(timeStamp)
}