package com.android.usemoney.di


import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.widget.Button
import com.android.usemoney.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(FragmentComponent::class)
class CategoryButtonModule {

    @Provides
    fun provideCategoryButton(@ApplicationContext appContext: Context): Button {
        return Button(appContext, null, R.style.categoryButton)
    }
    @Provides
    fun provideShape():GradientDrawable{
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = 50f
        return shape
    }
}