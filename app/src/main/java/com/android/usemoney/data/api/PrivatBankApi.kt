package com.android.usemoney.data.api

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PrivatBankApi {
    @POST("rest_fiz")
    fun getTransaction(@Body body: RequestBody): Call<String>

    @GET("exchange_rates?json")
    fun getCurrency(@Query("date") date:String):Call<ExchangeRate>
}