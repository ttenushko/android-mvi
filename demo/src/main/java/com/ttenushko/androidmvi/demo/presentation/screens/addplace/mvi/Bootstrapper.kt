package com.ttenushko.androidmvi.demo.presentation.screens.addplace.mvi

import com.ttenushko.androidmvi.demo.presentation.screens.addplace.mvi.Store.Event
import com.ttenushko.androidmvi.demo.presentation.screens.addplace.mvi.Store.State
import com.ttenushko.mvi.mviBootstrapper

internal fun bootstrapper() =
    mviBootstrapper<Action, State, Event> { state, actionDispatcher, _ ->
        actionDispatcher.dispatch(Action.SearchChanged(state.search))
    }
