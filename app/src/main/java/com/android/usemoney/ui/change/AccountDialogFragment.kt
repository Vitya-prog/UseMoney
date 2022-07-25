package com.android.usemoney.ui.change

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.DialogFragment
import com.android.usemoney.R

class AccountDialogFragment:DialogFragment()
{
    private lateinit var listViewAccount:ListView
    private var account = arrayOf("Наличные", "Карта")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_fragment_account,container,false)
        listViewAccount = view.findViewById(R.id.account_list_view)
        val adapter = ArrayAdapter(context!!,R.layout.list_view,account)
        listViewAccount.adapter = adapter
        return view
    }

}