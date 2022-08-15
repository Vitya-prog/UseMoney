package com.android.usemoney.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Singleton
    @Provides
    fun provideRequest(): Request {
        val soapRequest = " <request version=\"1.0\">\n" +
                "                <merchant>\n" +
                "                    <id>209275</id>\n" +
                "                    <signature>95c164fa5be9f84952fb53608e0bb5780ff5aa92</signature>\n" +
                "                </merchant>\n" +
                "<data><oper>cmt</oper><wait>0</wait><test>0</test><payment id=\"\"><prop name=\"sd\" value=\"01.08.2022\" /><prop name=\"ed\" value=\"10.08.2022\" /><prop name=\"card\" value=\"4149499396308270\" /></payment></data>" +
                "            </request>"
        val mediaType = MediaType.parse("text/xml")
        val body = RequestBody.create(mediaType, soapRequest)
        return Request.Builder()
            .url("https://api.privatbank.ua/p24api/rest_fiz")
            .post(body)
            .addHeader("content-type", "text/xml")
            .build()
    }

    @Provides
    fun provideClient():OkHttpClient{
        return OkHttpClient.Builder().build()
    }
}
