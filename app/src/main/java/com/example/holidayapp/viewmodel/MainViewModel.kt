package com.example.holidayapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.holidayapp.PublicHoliday
import com.example.holidayapp.repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _holidayLiveData: MutableLiveData<HolidayScreenState> = MutableLiveData()
    val holidayLiveData: LiveData<HolidayScreenState> = _holidayLiveData

    fun getHolidays() {
        viewModelScope.launch {
            val holidaysResult = mainRepository.getHolidays()
            holidaysResult.fold(
                onSuccess = {
                    _holidayLiveData.value = HolidayScreenState.Success(holidayList = it)
                },
                onFailure = {
                    // todo show failure state on screen
                    _holidayLiveData.value =
                        HolidayScreenState.Failure
                }
            )
        }
    }
}

sealed class HolidayScreenState {
    object Failure : HolidayScreenState()
    data class Success(val holidayList: List<PublicHoliday>) : HolidayScreenState()
}