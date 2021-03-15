package com.ttenushko.androidmvi.demo.presentation.screens.places.mvi

import com.ttenushko.androidmvi.demo.domain.application.usecase.TrackSavedPlacesUseCase
import com.ttenushko.androidmvi.demo.domain.utils.Either
import com.ttenushko.androidmvi.demo.presentation.utils.usecase.createExecutor
import com.ttenushko.androidmvi.demo.presentation.utils.usecase.multiResultUseCaseProvider
import com.ttenushko.mvi.MviMiddleware
import com.ttenushko.mvi.MviMiddlewareImpl

internal class TrackSavedPlacesMiddleware(
    private val trackSavedPlacesUseCase: TrackSavedPlacesUseCase
) : MviMiddlewareImpl<Action, Store.State, Store.Event>() {

    private val taskTrackSavedPlaces = createTrackSavedPlacesTask()

    override fun onApply(chain: MviMiddleware.Chain<Action, Store.State, Store.Event>) {
        when (chain.action) {
            is Action.Initialize -> {
                if (!taskTrackSavedPlaces.isRunning) {
                    taskTrackSavedPlaces.start(TrackSavedPlacesUseCase.Param(), Unit)
                }
                chain.proceed()
            }
            else -> chain.proceed()
        }
    }

    override fun onClose() {
        taskTrackSavedPlaces.stop()
    }

    private fun createTrackSavedPlacesTask() =
        createExecutor<TrackSavedPlacesUseCase.Param, TrackSavedPlacesUseCase.Result, Unit>(
            multiResultUseCaseProvider { _, _ ->
                actionDispatcher.dispatch(Action.LoadingSavedPlaces)
                trackSavedPlacesUseCase
            },
            { result, _ ->
                actionDispatcher.dispatch(Action.SavedPlacesUpdated(Either.Right(result.places)))
            },
            { error, _ ->
                actionDispatcher.dispatch(Action.SavedPlacesUpdated(Either.Left(error)))
            }
        )
}