package com.android.usemoney.ui.bill.add


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.android.usemoney.R

class IntroductionDialogFragment:DialogFragment() {
    private lateinit var idTextView: TextView
    private lateinit var okeyButton: Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.dialog_fragment_introduction,container,false)
        idTextView = view.findViewById(R.id.ipTextView)
        idTextView.setOnClickListener {
            val idLink = Uri.parse("https://2ip.ua/ru/")
            val intent = Intent(Intent.ACTION_VIEW,idLink)
            startActivity(intent)
        }

        okeyButton = view.findViewById(R.id.okeyButton)
        okeyButton.setOnClickListener {
            dismiss()
        }

        return view
    }

    companion object {
        const val TAG = "PurchaseConfirmationDialog"
    }
}