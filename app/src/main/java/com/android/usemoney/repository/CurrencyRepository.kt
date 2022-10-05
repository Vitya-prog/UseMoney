package com.android.usemoney.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.usemoney.data.api.Currency
import com.android.usemoney.data.api.ExchangeRate
import com.android.usemoney.data.api.PrivatBankApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.math.roundToInt


private const val TAG = "CurrencyRepository"
class CurrencyRepository @Inject constructor(
    @Named("currency")
    private val privatBankApi: PrivatBankApi
)   {

    fun getCurrency(): LiveData<List<Currency>> {
        val currencyLiveData:MutableLiveData<List<Currency>> = MutableLiveData()
        val date = SimpleDateFormat("dd.MM.yyyy").format(Date())
        val currencyRequest: Call<ExchangeRate> = privatBankApi.getCurrency(date)
        currencyRequest.enqueue(object:Callback<ExchangeRate>{
            override fun onResponse(call: Call<ExchangeRate>, response: Response<ExchangeRate>) {
                val exchangeRate = response.body()
                var currencyItem = exchangeRate?.exchangeRate
                currencyItem = currencyItem?.filter { currency ->
                            currency.currency == "USD" ||
                            currency.currency == "GBP" ||
                            currency.currency == "EUR"
                }
                currencyItem?.forEach { it.sale = ((1.0 / it.sale) *10000.0).roundToInt() / 10000.0 }
                currencyLiveData.value = currencyItem ?: mutableListOf()

            }

            override fun onFailure(call: Call<ExchangeRate>, t: Throwable) {
                Log.d(TAG,"Failure $t")
            }

        })
        return currencyLiveData
    }
}

