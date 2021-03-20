package com.ttenushko.androidmvi.demo.presentation.screens.placedetails.mvi

import com.ttenushko.androidmvi.demo.presentation.screens.placedetails.mvi.Store.Event
import com.ttenushko.androidmvi.demo.presentation.screens.placedetails.mvi.Store.State
import com.ttenushko.mvi.MviBootstrapper

internal fun bootstrapper() =
    MviBootstrapper<Action, State, Event> { state, actionDispatcher, _ ->
        when {
            null != state.error -> {
                // do nothing
            }
            null == state.weather -> {
                actionDispatcher.dispatch(Action.Reload)
            }
            else -> {
                // do nothing
            }
        }
    }

