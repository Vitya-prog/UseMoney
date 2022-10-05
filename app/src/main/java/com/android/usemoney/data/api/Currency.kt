package com.android.usemoney.data.api

import com.google.gson.annotations.SerializedName

data class Currency (
 @SerializedName("baseCurrency")
 var base:String,
 @SerializedName("currency")
 var currency:String,
 @SerializedName("saleRateNB")
 var sale:Double,
 )