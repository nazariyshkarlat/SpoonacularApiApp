package com.app.fruitcocktail.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.app.data.net.connection.ConnectionManager
import com.app.data.net.connection.ConnectionManagerImpl
import com.app.fruitcocktail.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {

    private const val BASE_URL = "https://api.spoonacular.com"

    lateinit var connectionManager: ConnectionManager
    private lateinit var retrofit: Retrofit

    lateinit var context: Context

    fun initialize(app: Application) {
        context = app
        connectionManager =
            ConnectionManagerImpl(
                getConnectivityManager(app)
            )
        retrofit =
            getRetrofit(
                getOkHttpClient(
                    getInterceptor()
                )
            )
    }

    fun <T> getService(className: Class<T>): T = retrofit.create(className)

    private fun getConnectivityManager(context: Context) =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

    private fun getRetrofit(client: OkHttpClient) =
        Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(BASE_URL)
            .build()

    private fun getOkHttpClient(interceptor: okhttp3.Interceptor) =
        OkHttpClient().newBuilder()
            .addInterceptor(interceptor)
            .readTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(2, TimeUnit.SECONDS)
            .build()


    private fun getInterceptor(): okhttp3.Interceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BASIC
            else
                HttpLoggingInterceptor.Level.NONE
        }
}