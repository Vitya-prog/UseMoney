package com.android.usemoney.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.android.usemoney.data.database.UseMoneyDataBase
import com.android.usemoney.data.database.dao.BillDao
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
    fun provideDatabase(application: Application): UseMoneyDataBase {
        val callback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "INSERT INTO category(id,name,type,value,icon,color) " +
                            "VALUES('123e4567-e89b-12d3-a456-426614174000','Подарки','Расходы',0.0,'gift_icon','#C71585')"
                )
                db.execSQL(
                    "INSERT INTO category(id,name,type,value,icon,color) " +
                            "VALUES('1cc00698-2cf5-11ed-a261-0242ac120002','Еда','Расходы',0.0,'food_icon','#228B22')"
                )
                db.execSQL(
                    "INSERT INTO category(id,name,type,value,icon,color) " +
                            "VALUES('37190206-2cf5-11ed-a261-0242ac120002','Семья','Расходы',0.0,'family_icon','#FF8A65')"
                )
                db.execSQL(
                    "INSERT INTO category(id,name,type,value,icon,color) " +
                            "VALUES('474eecd0-2cf5-11ed-a261-0242ac120002','Кафе','Расходы',0.0,'cafe_icon','#FFF176')"
                )
                db.execSQL(
                    "INSERT INTO category(id,name,type,value,icon,color) " +
                            "VALUES('513e7b3e-2cf5-11ed-a261-0242ac120002','Здоровье','Расходы',0.0,'health_icon','#4DD0E1')"
                )
                db.execSQL(
                    "INSERT INTO category(id,name,type,value,icon,color) " +
                            "VALUES('61b9f68c-2cf5-11ed-a261-0242ac120002','Транспорт','Расходы',0.0,'transport_icon','#FFDEAD')"
                )
                db.execSQL(
                    "INSERT INTO category(id,name,type,value,icon,color) " +
                            "VALUES('7c698b50-2cf5-11ed-a261-0242ac120002','Зарплата','Доходы',0.0,'category_salary_icon','#DAA520')"
                )
                db.execSQL(
                    "INSERT INTO category(id,name,type,value,icon,color) " +
                            "VALUES('9df5ea34-2cf5-11ed-a261-0242ac120002','Депозит','Доходы',0.0,'category_deposit_icon','#87CEFA')"
                )
                db.execSQL(
                    "INSERT INTO category(id,name,type,value,icon,color) " +
                            "VALUES('cc18a9ba-2cf5-11ed-a261-0242ac120002','Другие','Расходы',0.0,'unknown_icon','#A9A9A7')"
                )
                db.execSQL(
                    "INSERT INTO category(id,name,type,value,icon,color) " +
                            "VALUES('d282e4be-2cf5-11ed-a261-0242ac120002','Другие','Доходы',0.0,'unknown_icon','#A9A9A7')"
                )
            }
        }

        return Room.databaseBuilder(
            application.applicationContext,
            UseMoneyDataBase::class.java,
            "usemoney-database"
        )
            .addCallback(callback)
            .build()
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
    fun provideBillDao(useMoneyDataBase: UseMoneyDataBase): BillDao {
        return useMoneyDataBase.billDao()
    }
    @Provides
    fun provideExecutor():ExecutorService{
        return Executors.newSingleThreadExecutor()
    }



}