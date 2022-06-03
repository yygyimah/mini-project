package com.gyimah.lavori.utils

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.gyimah.lavori.R

object AppUtils {

    fun createLoadingDialog(
        context: Activity,
        text: String = "Loading, please wait..."
    ): AlertDialog {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(context)
        val inflater: LayoutInflater = context.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.alert_loading, null)
        val message: TextView = dialogView.findViewById(R.id.message)
        message.text = text
        alertDialog.setView(dialogView)
        alertDialog.setCancelable(false)
        return alertDialog.create()
    }
}