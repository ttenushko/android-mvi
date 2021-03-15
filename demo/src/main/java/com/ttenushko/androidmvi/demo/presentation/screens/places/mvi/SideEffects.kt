package com.ttenushko.androidmvi.demo.presentation.screens.places.mvi

import com.ttenushko.androidmvi.demo.presentation.screens.AppRouter
import com.ttenushko.androidmvi.demo.presentation.screens.places.mvi.Store.Event
import com.ttenushko.androidmvi.demo.presentation.screens.places.mvi.Store.State
import com.ttenushko.mvi.mviPostProcessor

internal fun sideEffects() =
    mviPostProcessor<Action, State, Event> { action, _, _, _, eventDispatcher ->
        when (action) {
            is Action.AddPlaceButtonClicked -> {
                eventDispatcher.dispatch(Event.Navigation(AppRouter.Destination.AddPlace("")))
            }
            is Action.PlaceClicked -> {
                eventDispatcher.dispatch(Event.Navigation(AppRouter.Destination.PlaceDetails(action.place.id)))
            }
            else -> {
                // do nothing
            }
        }
    }
