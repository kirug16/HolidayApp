package com.example.holidayapp.repository

import com.example.holidayapp.PublicHoliday
import com.example.holidayapp.remote.HolidayResponse
import com.example.holidayapp.remote.HolidayService
import retrofit2.Response


class MainRepository(
    private val holidayService: HolidayService
) {
    suspend fun getHolidays(): Result<List<PublicHoliday>> {
        try {
            val result: Response<HolidayResponse> = holidayService.instance.getPublicHolidays()
            return if (result.isSuccessful && result.body() != null) {
                val listOfHolidays = result.body()!!.map {
                    PublicHoliday(
                        name = it.name,
                        date = it.date
                    )
                }
                Result.success(listOfHolidays)
            } else {
                //failure
                Result.failure(Throwable("Successful fetch, but null response body"))
            }
        } catch (e: Exception) {
            return Result.failure(Throwable("failed to fetch holidays; $e"))
        }
    }
}