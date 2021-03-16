package com.ttenushko.androidmvi.demo.presentation.screens.addplace.mvi


import com.ttenushko.androidmvi.demo.presentation.screens.addplace.mvi.Store.State
import com.ttenushko.mvi.mviReducer

internal fun reducer() =
    mviReducer<Action, State> { action, state ->
        var updatedState = when (action) {
            is Action.SearchChanged -> {
                if (state.search != action.search) {
                    state.copy(
                        search = action.search,
                        isSearching = true
                    )
                } else state
            }
            is Action.SearchComplete -> {
                if (state.search == action.searchResult.search) {
                    state.copy(
                        searchResult = action.searchResult,
                        isSearching = false
                    )
                } else state
            }
            else -> state
        }
        val isSearching = updatedState.search != updatedState.searchResult.search
        if (isSearching != updatedState.isSearching) {
            updatedState = updatedState.copy(isSearching = isSearching)
        }
        updatedState
    }