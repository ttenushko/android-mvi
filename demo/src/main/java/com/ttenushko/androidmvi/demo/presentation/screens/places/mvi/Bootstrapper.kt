package com.ttenushko.androidmvi.demo.presentation.screens.places.mvi

import com.ttenushko.androidmvi.demo.presentation.screens.places.mvi.Store.Event
import com.ttenushko.androidmvi.demo.presentation.screens.places.mvi.Store.State
import com.ttenushko.mvi.MviBootstrapper

internal fun bootstrapper() =
    MviBootstrapper<Action, State, Event> { _, actionDispatcher, _ ->
        actionDispatcher.dispatch(Action.Initialize)
    }
