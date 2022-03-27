package com.example.holidayapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.holidayapp.PublicHoliday
import com.example.holidayapp.remote.HolidayService
import com.example.holidayapp.repository.MainRepository
import com.example.holidayapp.ui.theme.HolidayAppTheme
import com.example.holidayapp.viewmodel.HolidayScreenState
import com.example.holidayapp.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalUnitApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HolidayAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val mainViewModel =
                        MainViewModel(mainRepository = MainRepository(holidayService = HolidayService))

                    mainViewModel.getHolidays()
                    Column() {
                        Text(
                            text = "Holiday in 2022",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(8.dp),
                            fontSize = TextUnit(
                                value = 32f, type =
                                TextUnitType.Sp
                            )
                        )
                        HolidayScreen(viewModel = mainViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun HolidayScreen(viewModel: MainViewModel) {
    val holidays: HolidayScreenState by viewModel.holidayLiveData.observeAsState(
        initial = HolidayScreenState.Failure
    )
    HolidayList(holidays = holidays)
}

@Composable
fun HolidayList(holidays: HolidayScreenState) {
    LazyColumn {
        when (holidays) {
            is HolidayScreenState.Failure -> {
                item {
                    HolidayFailureScreenItem()
                }
            }
            is HolidayScreenState.Success -> {
                items(holidays.holidayList) {
                    HolidayScreenItem(
                        holiday = it
                    )
                }
            }
        }
    }
}

@Composable
fun HolidayScreenItem(holiday: PublicHoliday) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(Modifier.padding(8.dp))
    )
    {
        Text(text = holiday.name, fontWeight = FontWeight.Bold)
        Text(text = holiday.date)
    }
}

@OptIn(ExperimentalUnitApi::class)
@Composable
fun HolidayFailureScreenItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(Modifier.padding(8.dp))
    )
    {
        Text(
            text = "Failure to fetch data!",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp),
            fontSize = TextUnit(
                value = 32f, type =
                TextUnitType.Sp
            )
        )
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HolidayAppTheme {
        Greeting("Android")
    }
}