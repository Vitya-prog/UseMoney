package com.android.usemoney.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.android.usemoney.R
import com.android.usemoney.data.database.dao.ChangeDao
import com.android.usemoney.data.model.Change
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import okhttp3.*
import org.apache.commons.codec.digest.DigestUtils
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


private const val TAG = "TransactionsWorker"
@HiltWorker
class TransactionsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val changeDao:ChangeDao
): CoroutineWorker(context,params) {
    private val client = OkHttpClient.Builder().build()
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
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG,"Error:$e")
            }

            override fun onResponse(call: Call, response: Response) {
//                Log.d(TAG, response.peekBody(1000000).string())
                response1 = response.peekBody(1000000).string()
            }

        })
        var i = 0
        while(response1 == ""){
            i++
        }
        parseRequest(response1)

    }
    private fun createRequest(id:String,card:String,date:String,password: String): Request {
        val signature = DigestUtils.sha1Hex(DigestUtils.md5Hex("<oper>cmt</oper><wait>0</wait><test>0</test><payment id=\"\"><prop name=\"sd\" value=\"$date\" /><prop name=\"ed\" value=\"15.08.2022\" /><prop name=\"card\" value=\"$card\" /></payment>$password"))
        val soapRequest = " <request version=\"1.0\">\n" +
                "                <merchant>\n" +
                "                    <id>$id</id>\n" +
                "                    <signature>$signature</signature>\n" +
                "                </merchant>\n" +
                "<data><oper>cmt</oper><wait>0</wait><test>0</test><payment id=\"\"><prop name=\"sd\" value=\"$date\" /><prop name=\"ed\" value=\"15.08.2022\" /><prop name=\"card\" value=\"$card\" /></payment></data>" +
                "            </request>"
        val mediaType = MediaType.parse("text/xml")
        val body = RequestBody.create(mediaType, soapRequest)
        return Request.Builder()
            .url("https://api.privatbank.ua/p24api/rest_fiz")
            .post(body)
            .addHeader("content-type", "text/xml")
            .build()
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
//            Log.d(TAG,arr2.toString())
            val value = arr2[7].trim().replace("-","").replace("UAH","").toDouble()
            searchChange(value,
                SimpleDateFormat("yyyy-MM-dd").parse(arr2[3]), arr2[11].replace("]",""),arr2)

        }

    }

    private suspend fun searchChange(value: Double, date: Date?, replace: String, arr2:List<String>) {

            if (changeDao.getChangeByParam(value,date!!,replace).isEmpty()) {
                changeDao.addChange(
                    Change(
                    UUID.randomUUID(),
                    "Неизвестен",
                    value,
                    R.drawable.unknown_icon,
                    "#A9A9A9",
                    SimpleDateFormat("yyyy-MM-dd").parse(arr2[3]) ?: Date(),
                    "Расходы",
                    arr2[11].replace("]","")
                ))
            }
        }
}

//arr2 =[[ ,  4149499396308270 ,  239060 ,  2022-08-03 ,  03:48:00 ,  8.00 UAH ,  ,  -8.00 UAH ,  1530.00 UAH ,  PrivatBank,  CP980U48 ,  Округление остатка на Копилку
//    ]]