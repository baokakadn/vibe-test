package com.axonvibe.challenge.mobile.main.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.axonvibe.challenge.mobile.R

class WaitingQuestionDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.fragment_dialogue_waiting_finish_question_answer_screen, container)
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window.requestFeature(Window.FEATURE_NO_TITLE)
            val width = (resources.displayMetrics.widthPixels * DIALOG_RATIO).toInt()
            dialog!!.window.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        }
        return view
    }

    companion object {
        const val DIALOG_RATIO = 0.8
        const val TAG = "AlertDialog"
    }

}