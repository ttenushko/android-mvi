package com.ttenushko.androidmvi.demo.presentation.screens.addplace.mvi

import com.ttenushko.androidmvi.demo.presentation.screens.AppRouter
import com.ttenushko.androidmvi.demo.presentation.screens.addplace.mvi.Store.Event
import com.ttenushko.androidmvi.demo.presentation.screens.addplace.mvi.Store.State
import com.ttenushko.mvi.mviPostProcessor

internal fun sideEffects() =
    mviPostProcessor<Action, State, Event> { action, _, _, _, eventDispatcher ->
        when (action) {
            is Action.PlaceSaved -> {
                eventDispatcher.dispatch(Event.Navigation(AppRouter.Destination.GoBack))
            }
            else -> {
                // do nothing
            }
        }
    }
