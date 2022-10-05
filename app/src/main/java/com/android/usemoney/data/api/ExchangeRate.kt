package com.android.usemoney.data.api

import com.google.gson.annotations.SerializedName

class ExchangeRate {

    @SerializedName("exchangeRate")
    lateinit var exchangeRate: List<Currency>
}