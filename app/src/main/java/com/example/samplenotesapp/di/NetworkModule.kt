package com.example.samplenotesapp.di

import com.example.samplenotesapp.api.AuthInterceptor
import com.example.samplenotesapp.api.NotesApi
import com.example.samplenotesapp.api.UserApi
import com.example.samplenotesapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit() :Retrofit.Builder{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)

    }
    @Singleton
    @Provides
    fun provideOkhttpClient(authInterceptor: AuthInterceptor):OkHttpClient{
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Singleton
    @Provides
    fun provideUserApi(retrofitBuilder:Retrofit.Builder):UserApi{
        return retrofitBuilder.build().create(UserApi::class.java)
    }
    @Singleton
    @Provides
    fun providesAuthRetrofit(okHttpClient: OkHttpClient) :Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(Constants.BASE_URL)
            .build()
    }
    @Singleton
    @Provides
    fun provideNotesApi(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient) :NotesApi{
        return retrofitBuilder
            .client(okHttpClient)
            .build()
            .create(NotesApi::class.java)
    }

}