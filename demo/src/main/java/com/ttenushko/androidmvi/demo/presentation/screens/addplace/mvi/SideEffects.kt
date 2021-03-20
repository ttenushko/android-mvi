package com.ttenushko.androidmvi.demo.presentation.screens.addplace.mvi

import com.ttenushko.androidmvi.demo.presentation.screens.AppRouter
import com.ttenushko.androidmvi.demo.presentation.screens.addplace.mvi.Store.Event
import com.ttenushko.androidmvi.demo.presentation.screens.addplace.mvi.Store.State
import com.ttenushko.mvi.MviPostProcessorMiddleware

internal fun sideEffects() =
    MviPostProcessorMiddleware.PostProcessor<Action, State, Event> { action, _, _, _, eventDispatcher ->
        when (action) {
            is Action.PlaceSaved -> {
                eventDispatcher.dispatch(Event.Navigation(AppRouter.Destination.GoBack))
            }
            else -> {
                // do nothing
            }
        }
    }
