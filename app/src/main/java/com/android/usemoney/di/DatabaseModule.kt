package com.android.usemoney.di

import android.app.Application
import androidx.room.Room
import com.android.usemoney.data.database.UseMoneyDataBase
import com.android.usemoney.data.database.dao.CategoryDao
import com.android.usemoney.data.database.dao.ChangeDao
import com.android.usemoney.data.database.dao.PlanDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

@Singleton
@Provides
fun provideDatabase(application: Application):UseMoneyDataBase{
    return Room.databaseBuilder(
        application.applicationContext,
        UseMoneyDataBase::class.java,
        "usemoney-database"
    ).build()
}

    @Provides
    fun provideCategoryDao(useMoneyDataBase: UseMoneyDataBase):CategoryDao{
        return useMoneyDataBase.categoryDao()
    }
    @Provides
    fun provideChangeDao(useMoneyDataBase: UseMoneyDataBase): ChangeDao {
        return useMoneyDataBase.changeDao()
    }
    @Provides
    fun providePlanDao(useMoneyDataBase: UseMoneyDataBase): PlanDao {
        return useMoneyDataBase.planDao()
    }
    @Provides
    fun provideExecutor():ExecutorService{
        return Executors.newSingleThreadExecutor()
    }

}