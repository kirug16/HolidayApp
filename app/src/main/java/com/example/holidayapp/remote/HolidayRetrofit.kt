package com.example.holidayapp.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.*
import kotlin.collections.ArrayList

interface HolidayRetrofit {
    @GET("PublicHolidays/2022/US")
    suspend fun getPublicHolidays(): Response<HolidayResponse>
}

object HolidayService {

    lateinit var instance: HolidayRetrofit

    init {
        create()
    }

    private fun create(): HolidayRetrofit {
        val baseUrl = "https://date.nager.at/api/v3/"
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        instance = retrofit.create(HolidayRetrofit::class.java)
        return instance
    }
}


class HolidayResponse : ArrayList<HolidayResponse.HolidayItem>() {

    data class HolidayItem(
        @SerializedName("date")
        val date: String,
        @SerializedName("localName")
        val localName: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("countryCode")
        val countryCode: String,
    )

}