package com.ttenushko.androidmvi.demo.presentation.screens.addplace.mvi

import com.ttenushko.androidmvi.demo.domain.application.usecase.SavePlaceUseCase
import com.ttenushko.androidmvi.demo.domain.utils.Either
import com.ttenushko.androidmvi.demo.presentation.screens.addplace.mvi.Store.Event
import com.ttenushko.androidmvi.demo.presentation.screens.addplace.mvi.Store.State
import com.ttenushko.androidmvi.demo.presentation.utils.usecase.createExecutor
import com.ttenushko.androidmvi.demo.presentation.utils.usecase.singleResultUseCaseProvider
import com.ttenushko.mvi.MviMiddleware
import com.ttenushko.mvi.MviMiddlewareImpl

internal class AddPlaceMiddleware(
    private val savePlaceUseCase: SavePlaceUseCase
) : MviMiddlewareImpl<Action, State, Event>() {

    private val taskAddPlace = createSavePlaceTask()

    override fun onApply(chain: MviMiddleware.Chain<Action, State, Event>) {
        when (val action = chain.action) {
            is Action.PlaceClicked -> {
                if (!taskAddPlace.isRunning) {
                    taskAddPlace.start(SavePlaceUseCase.Param(action.place), Unit)
                }
            }
            else -> chain.proceed()
        }
    }

    override fun onClose() {
        taskAddPlace.stop()
    }

    private fun createSavePlaceTask() =
        createExecutor<SavePlaceUseCase.Param, SavePlaceUseCase.Result, Unit>(
            singleResultUseCaseProvider { _, _ -> savePlaceUseCase },
            { result, _ ->
                actionDispatcher.dispatch(
                    Action.PlaceSaved(
                        if (result.isAdded) Either.Right(result.place)
                        else Either.Left(IllegalStateException())
                    )
                )
            },
            { error, _ ->
                actionDispatcher.dispatch(
                    Action.PlaceSaved(Either.Left(error))
                )
            }
        )
}