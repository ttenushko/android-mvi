package com.ttenushko.androidmvi.demo.framework.android.weather.repository

import com.ttenushko.androidmvi.demo.data.weather.repository.WeatherForecastRepositoryImpl
import com.ttenushko.androidmvi.demo.domain.weather.model.Place
import com.ttenushko.androidmvi.demo.domain.weather.model.Weather
import com.ttenushko.androidmvi.demo.framework.android.weather.repository.rest.OpenWeatherMapApi
import com.ttenushko.androidmvi.demo.framework.android.weather.repository.rest.model.process
import com.ttenushko.androidmvi.demo.framework.android.weather.repository.rest.model.toDomainModel


class OpenWeatherMapDataSource(private val openWeatherMapApi: OpenWeatherMapApi) :
    WeatherForecastRepositoryImpl.DataSource {

    override fun findPlaces(query: String): List<Place> =
        openWeatherMapApi.find(query).process().let { response ->
            response.items.map { it.toDomainModel() }
        }

    override fun getWeatherById(placeId: Long): Weather =
        openWeatherMapApi.getWeather(placeId).process().toDomainModel()
}