package com.ttenushko.androidmvi.demo.di.data

import android.content.Context
import com.ttenushko.androidmvi.demo.Config
import com.ttenushko.androidmvi.demo.data.application.repository.ApplicationSettingsImpl
import com.ttenushko.androidmvi.demo.data.weather.repository.WeatherForecastRepositoryImpl
import com.ttenushko.androidmvi.demo.domain.application.repository.ApplicationSettings
import com.ttenushko.androidmvi.demo.domain.weather.repository.WeatherForecastRepository
import com.ttenushko.androidmvi.demo.framework.android.application.repository.AndroidApplicationSettings
import com.ttenushko.androidmvi.demo.framework.android.weather.repository.OpenWeatherMapDataSource
import com.ttenushko.androidmvi.demo.framework.android.weather.repository.rest.OpenWeatherMapApi
import com.ttenushko.androidmvi.demo.framework.android.weather.repository.rest.OpenWeatherMapApiFactory
import com.ttenushko.androidmvi.demo.presentation.utils.dagger.ApplicationScope
import dagger.Module
import dagger.Provides


@Module
class DataModule(
    private val isDebug: Boolean
) {
    @Provides
    @ApplicationScope
    fun provideApplicationSettings(context: Context): ApplicationSettings =
        ApplicationSettingsImpl(AndroidApplicationSettings(context))

    @Provides
    @ApplicationScope
    fun provideOpenWeatherMapApi(context: Context): OpenWeatherMapApi =
        OpenWeatherMapApiFactory.create(
            context,
            Config.OPEN_WEATHER_MAP_API_BASE_URL,
            Config.OPEN_WEATHER_MAP_API_KEY,
            isDebug
        )

    @Provides
    @ApplicationScope
    fun provideWeatherForecastRepository(dataSource: WeatherForecastRepositoryImpl.DataSource): WeatherForecastRepository =
        WeatherForecastRepositoryImpl(dataSource)

    @Provides
    @ApplicationScope
    fun provideWeatherForecastDataSource(openWeatherMapApi: OpenWeatherMapApi): WeatherForecastRepositoryImpl.DataSource =
        OpenWeatherMapDataSource(openWeatherMapApi)
}