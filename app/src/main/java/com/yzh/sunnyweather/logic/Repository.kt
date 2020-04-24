package com.yzh.sunnyweather.logic

import androidx.lifecycle.liveData
import com.yzh.sunnyweather.logic.dao.PlaceDao
import com.yzh.sunnyweather.logic.model.Place
import com.yzh.sunnyweather.logic.model.Weather
import com.yzh.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext


object Repository {

    fun savePlace(place: Place)=PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved()=PlaceDao.isPlaceSaved()

    fun searchPlaces(query: String) = fire(Dispatchers.IO) {

        val placeRespsonse = SunnyWeatherNetwork.searchPlaces(query)
        if (placeRespsonse.status == "ok") {
            Result.success(placeRespsonse.places)

        } else {

            Result.failure(RuntimeException("response status is ${placeRespsonse.status}"))
        }
    }



    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealtime = async {
                SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
            }
            val deferredDaily = async {
                SunnyWeatherNetwork.getDailyWeather(lng, lat)
            }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather =
                    Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException("realtime response status is ${realtimeResponse.status}" + "daily Reponse status is ${dailyResponse.status}")
                )
            }
        }

    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }
}

