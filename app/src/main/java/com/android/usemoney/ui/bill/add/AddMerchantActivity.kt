package com.android.usemoney.ui.bill.add


import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.android.usemoney.R


private const val TAG = "AddMerchantActivity"
class AddMerchantActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IntroductionDialogFragment().show(
            supportFragmentManager,IntroductionDialogFragment.TAG
        )
        setContentView(R.layout.activity_add_merchant)
        val webView: WebView = findViewById(R.id.webview)
        webView.settings.javaScriptEnabled = true
        webView.loadUrl("https://next.privat24.ua/vacancies")
    }
}
///%2Fvacancies