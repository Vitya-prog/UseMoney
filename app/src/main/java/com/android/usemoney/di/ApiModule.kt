package com.android.usemoney.di

import com.android.usemoney.data.api.PrivatBankApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Named("transactions")
    fun provideTransactionsRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.privatbank.ua/p24api/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    @Provides
    @Named("currency")
    fun provideCurrencyRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.privatbank.ua/p24api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    @Named("transactions")
    fun provideTransactionsPrivatBankApi(@Named("transactions")retrofit: Retrofit):PrivatBankApi {
        return retrofit.create(PrivatBankApi::class.java)
    }

    @Provides
    @Named("currency")
    fun provideCurrencyPrivatBankApi(@Named("currency")retrofit: Retrofit):PrivatBankApi {
        return retrofit.create(PrivatBankApi::class.java)
    }
}
