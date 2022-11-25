package com.android.usemoney.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.android.usemoney.R
import com.android.usemoney.data.api.PrivatBankApi
import com.android.usemoney.data.database.dao.ChangeDao
import com.android.usemoney.data.local.Change
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import okhttp3.MediaType
import okhttp3.RequestBody
import org.apache.commons.codec.digest.DigestUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Named


private const val TAG = "TransactionsWorker"
@HiltWorker
class TransactionsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val changeDao:ChangeDao,
    @Named("transactions")
    private val privatApi:PrivatBankApi
): CoroutineWorker(context,params) {
    override suspend fun doWork(): Result {
        return try {
            val id = inputData.getString("keyId")!!
            val card = inputData.getString("keyCard")!!
            val password = inputData.getString("keyPassword")!!
            val date = inputData.getString("keyDate")!!
            Log.d(TAG,"id=$id,card=$card,password=$password,date=$date")
            getTransaction(id,card,date,password)
            Result.success()
        }
        catch(t:Throwable){
            Log.d(TAG,"Error:$t")
            Result.failure()
        }
    }

    private suspend fun getTransaction(id: String, card: String, date: String, password: String){
        val request = createRequest(id,card,date,password)
        var response1 = ""
        request.enqueue(object:Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d(TAG,"${response.body()}")
               response1 = response.body().toString()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d(TAG,"Error: $t")
            }

        })
        var i = 0
        while(response1 == ""){
            i++
        }
        parseRequest(response1)

    }
    private fun createRequest(
        id: String,
        card: String,
        date: String,
        password: String
    ): Call<String> {
        val today = SimpleDateFormat("dd.MM.yyyy").format(Date())
        val signature =
            DigestUtils.sha1Hex(DigestUtils.md5Hex("<oper>cmt</oper><wait>0</wait><test>0</test><payment id=\"\"><prop name=\"sd\" value=\"$date\" /><prop name=\"ed\" value=\"$today\" /><prop name=\"card\" value=\"$card\" /></payment>$password"))
        val soapRequest = " <request version=\"1.0\">\n" +
                "                <merchant>\n" +
                "                    <id>$id</id>\n" +
                "                    <signature>$signature</signature>\n" +
                "                </merchant>\n" +
                "<data><oper>cmt</oper><wait>0</wait><test>0</test><payment id=\"\"><prop name=\"sd\" value=\"$date\" /><prop name=\"ed\" value=\"$today\" /><prop name=\"card\" value=\"$card\" /></payment></data>" +
                "            </request>"
        val mediaType = MediaType.parse("text/xml")
        val body = RequestBody.create(mediaType, soapRequest)
        return privatApi.getTransaction(body)
    }
    private suspend fun parseRequest(s: String) {
        val arr = s.split("<statement")
        for (i in 2..arr.size-1){
//            Log.d(TAG,arr[i])
            val arr1 = arr[i].split("card",
                "appcode",
                "trandate",
                "trantime",
                "amount",
                "cardamount",
                "rest",
                "terminal",
                "description").toMutableList()
            val arr2 =arr1.toString().replace("/>","")
                .replace("\"","")
                .replace("=","")
                .replace("</statements></info></data></response>]","").split(",")
            Log.d(TAG,arr2.toString())
            val value = arr2[7].trim().replace("-","").replace("UAH","")
            val type = if (arr2[7].trim().replace("UAH","")[0] == '-') "Расходы" else "Доходы"
            Log.d(TAG, (arr2[3]+arr2[4]).trim())
            searchChange(value.toDouble(),
                SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").parse((arr2[3]+arr2[4]).trim())!!,arr2,type)
        }

    }

    private suspend fun searchChange(value: Double, date: Date, arr2:List<String>,type:String) {
            if (changeDao.getChangeByParam(arr2[2].trim()).isEmpty()) {
                Log.d(TAG,arr2[2].trim())
                changeDao.addChange(
                    Change(
                    UUID.randomUUID(),
                    "Другие",
                    value,
                    R.drawable.unknown_icon,
                    "#A9A9A9",
                    date,                    type,
                    arr2[11].replace("]",""),
                    arr2[2].trim()
                ))
            }
        }
}
