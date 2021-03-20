package com.ttenushko.androidmvi.demo.presentation.screens.placedetails.mvi

import com.ttenushko.androidmvi.demo.domain.weather.model.Weather
import com.ttenushko.androidmvi.demo.presentation.screens.AppRouter
import com.ttenushko.mvi.MviStore

interface Store :
    MviStore<Store.Intention, Store.State, Store.Event> {

    data class State(
        val placeId: Long,
        val weather: Weather?,
        val error: Throwable?,
        val isLoading: Boolean,
        val isDeleting: Boolean
    ) {
        val isDeleteButtonVisible: Boolean
            get() = null != weather
    }

    sealed class Intention {
        object DeleteClicked : Intention()
        object Refresh : Intention()
        object DeleteConfirmed : Intention()
    }

    sealed class Event {
        object ShowDeleteConfirmation : Event()
        data class ShowError(val error: Throwable) : Event()
        data class Navigation(val destination: AppRouter.Destination) : Event()
    }
}