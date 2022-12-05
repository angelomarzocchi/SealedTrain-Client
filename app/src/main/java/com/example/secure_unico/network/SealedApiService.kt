package com.example.secure_unico.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response


import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL =
    "http://192.168.1.32:8080"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()




private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface SealedApiService {

    @POST("signin")
    suspend fun getToken(@Body request: LoginRequest): TokenResponse

    @GET("authenticate")
    suspend fun authenticate(@Header("Authorization") token:String)

    @GET("tickets")
    suspend fun getTickets(@Header("Authorization") token:String) : List<Ticket>

}

object SealedApi {
    val retrofitService: SealedApiService by lazy {
        retrofit.create(SealedApiService::class.java)
    }
}