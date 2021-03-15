package com.ttenushko.androidmvi.demo.presentation.screens.places.mvi

import com.ttenushko.androidmvi.demo.presentation.screens.places.mvi.Store.State
import com.ttenushko.mvi.mviBootstrapper

internal fun bootstrapper() =
    mviBootstrapper<Action, State, Store.Event> { _, actionDispatcher, _ ->
        actionDispatcher.dispatch(Action.Initialize)
    }
