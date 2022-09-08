package com.android.usemoney.di



import android.graphics.drawable.GradientDrawable
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
class CategoryButtonModule {

    @Provides
    fun provideShape():GradientDrawable{
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = 50f
        return shape
    }
}