package com.ttenushko.androidmvi.demo.presentation.screens.home.places.mvi

import com.ttenushko.androidmvi.demo.domain.utils.Either
import com.ttenushko.androidmvi.demo.presentation.screens.home.places.mvi.Store.State
import com.ttenushko.mvi.mviReducer

internal fun reducer() =
    mviReducer<Action, State> { action, state ->
        when (action) {
            is Action.Initialize -> {
                state.copy(isLoading = false)
            }
            is Action.LoadingSavedPlaces -> {
                state.copy(isLoading = true)
            }
            is Action.SavedPlacesUpdated -> {
                when (action.result) {
                    is Either.Right -> {
                        state.copy(places = action.result.value, error = null, isLoading = false)
                    }
                    is Either.Left -> {
                        state.copy(places = null, error = action.result.value, isLoading = false)
                    }
                }
            }
            else -> state
        }
    }