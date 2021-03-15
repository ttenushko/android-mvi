package com.ttenushko.androidmvi.demo.di

import android.content.Context
import com.squareup.picasso.Picasso
import com.ttenushko.androidmvi.demo.domain.application.repository.ApplicationSettings
import com.ttenushko.androidmvi.demo.domain.weather.repository.WeatherForecastRepository
import com.ttenushko.androidmvi.demo.presentation.base.Router
import com.ttenushko.androidmvi.demo.presentation.screens.AppRouter
import com.ttenushko.androidmvi.demo.presentation.utils.MviEventLogger
import com.ttenushko.androidmvi.demo.presentation.utils.MviLogger
import com.ttenushko.androidmvi.demo.presentation.utils.dagger.ApplicationContext


interface PublicDependencies : Dependency {
    fun applicationSettings(): ApplicationSettings
    fun weatherForecastRepository(): WeatherForecastRepository
    fun picasso(): Picasso
    fun router(): Router<AppRouter.Destination>

    @ApplicationContext
    fun context(): Context
}