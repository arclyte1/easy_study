package com.example.easy_study.di

import com.example.easy_study.common.Constants
import com.example.easy_study.data.remote.EasyStudyApi
import com.example.easy_study.data.repository.GroupRepositoryImpl
import com.example.easy_study.data.repository.LoginRepositoryImpl
import com.example.easy_study.domain.repository.GroupRepository
import com.example.easy_study.domain.repository.LoginRepository
import com.example.easy_study.presentation.navigation.AppNavigator
import com.example.easy_study.presentation.navigation.AppNavigatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideEasyStudyApi(): EasyStudyApi =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EasyStudyApi::class.java)

    @Provides
    @Singleton
    fun provideLoginRepository(api: EasyStudyApi): LoginRepository =
        LoginRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideGroupRepository(api: EasyStudyApi, loginRepository: LoginRepository): GroupRepository =
        GroupRepositoryImpl(api, loginRepository)

    @Provides
    @Singleton
    fun provideAppNavigator(appNavigatorImpl: AppNavigatorImpl): AppNavigator = appNavigatorImpl
}