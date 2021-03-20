package com.ttenushko.androidmvi.demo.presentation.screens.placedetails.mvi

import com.ttenushko.androidmvi.demo.domain.utils.Either
import com.ttenushko.androidmvi.demo.presentation.screens.AppRouter
import com.ttenushko.androidmvi.demo.presentation.screens.placedetails.mvi.Store.Event
import com.ttenushko.androidmvi.demo.presentation.screens.placedetails.mvi.Store.State
import com.ttenushko.mvi.MviPostProcessorMiddleware

internal fun sideEffects() =
    MviPostProcessorMiddleware.PostProcessor<Action, State, Event> { action, _, _, _, eventDispatcher ->
        when (action) {
            is Action.DeleteClicked -> {
                eventDispatcher.dispatch(Event.ShowDeleteConfirmation)
            }
            is Action.Deleted -> {
                when (action.result) {
                    is Either.Right -> {
                        eventDispatcher.dispatch(Event.Navigation(AppRouter.Destination.GoBack))
                    }
                    is Either.Left -> {
                        eventDispatcher.dispatch(Event.ShowError(action.result.value))
                    }
                }
            }
            else -> {
                // do nothing
            }
        }
    }