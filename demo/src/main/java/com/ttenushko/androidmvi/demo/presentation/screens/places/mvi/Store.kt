package com.ttenushko.androidmvi.demo.presentation.screens.places.mvi

import com.ttenushko.androidmvi.demo.domain.weather.model.Place
import com.ttenushko.androidmvi.demo.presentation.screens.AppRouter
import com.ttenushko.androidmvi.demo.presentation.screens.places.mvi.Store.*
import com.ttenushko.mvi.MviStore

interface Store : MviStore<Intention, State, Event> {

    data class State(
        val places: List<Place>?,
        val error: Throwable?,
        val isLoading: Boolean
    )

    sealed class Intention {
        object AddPlaceButtonClicked : Intention()
        data class PlaceClicked(val place: Place) : Intention()
    }

    sealed class Event {
        data class Navigation(val destination: AppRouter.Destination) : Event()
    }
}