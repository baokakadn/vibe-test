package com.axonvibe.challenge.mobile.main.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class AlertMessageDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val message = requireArguments().getString(MESSAGE)
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle(TITLE)
        alertDialogBuilder.setMessage(message)
        alertDialogBuilder.setPositiveButton(BUTTON) { dialog, which -> dialog.dismiss() }
        return alertDialogBuilder.create()
    }

    companion object {
        fun newInstance(message: String): AlertMessageDialogFragment {
            val frag = AlertMessageDialogFragment()
            val args = Bundle()
            args.putString(MESSAGE, message)
            frag.arguments = args
            return frag
        }

        const val MESSAGE = "message"
        const val TITLE = "Alert"
        const val BUTTON = "OK"
        const val TAG = "MessageDialog"
    }
}