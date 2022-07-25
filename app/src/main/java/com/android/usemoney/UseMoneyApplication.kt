package com.android.usemoney

import android.app.Application
import com.android.usemoney.repository.UseMoneyRepository

class UseMoneyApplication: Application()
{
    override fun onCreate() {
        super.onCreate()
        UseMoneyRepository.initialize(this)
    }
}

