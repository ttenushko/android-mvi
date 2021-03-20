package com.ttenushko.androidmvi.demo.presentation.screens.placedetails.mvi

import com.ttenushko.androidmvi.demo.domain.utils.Either
import com.ttenushko.androidmvi.demo.presentation.screens.placedetails.mvi.Store.State
import com.ttenushko.mvi.MviReducer

internal fun reducer() =
    MviReducer<Action, State> { action, state ->
        when (action) {
            is Action.Reloading -> {
                state.copy(isLoading = true)
            }
            is Action.Reloaded -> {
                when (val result = action.result) {
                    is Either.Right -> {
                        state.copy(
                            weather = result.value,
                            error = null,
                            isLoading = false
                        )
                    }
                    is Either.Left -> {
                        state.copy(
                            weather = null,
                            error = result.value,
                            isLoading = false
                        )
                    }
                }
            }
            is Action.Deleting -> {
                state.copy(isDeleting = true)
            }
            is Action.Deleted -> {
                when (action.result) {
                    is Either.Right -> {
                        state.copy(
                            isDeleting = false
                        )
                    }
                    is Either.Left -> {
                        state.copy(
                            isDeleting = false
                        )
                    }
                }
            }
            else -> state
        }
    }