package com.yzh.sunnyweather.logic

import androidx.lifecycle.liveData
import com.yzh.sunnyweather.logic.model.Place
import com.yzh.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.RuntimeException


object Repository {
    fun  searchPlaces(query:String)= liveData(context=Dispatchers.IO){
        val result = try {
            val placeRespsonse = SunnyWeatherNetwork.searchPlaces(query)
            if (placeRespsonse.status == "ok") {
                Result.success(placeRespsonse.places)
            } else {

                Result.failure(RuntimeException("response status is ${placeRespsonse.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<Place>>(e)
        }
       emit(result)

    }

}