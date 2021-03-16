package com.ttenushko.androidmvi.demo.presentation.screens.places

import android.os.Bundle
import com.ttenushko.androidmvi.demo.domain.application.usecase.TrackSavedPlacesUseCase
import com.ttenushko.androidmvi.demo.presentation.screens.places.mvi.*
import com.ttenushko.androidmvi.demo.presentation.screens.places.mvi.Store.*
import com.ttenushko.androidmvi.demo.presentation.utils.MviLogger
import com.ttenushko.mvi.*
import com.ttenushko.mvi.android.MviStoreViewModel

internal class PlacesViewModel(
    private val mviLogger: MviLogger<Action, State>,
    private val trackSavedPlacesUseCase: TrackSavedPlacesUseCase,
    savedState: Bundle?
) : MviStoreViewModel<Intention, State, Event>(savedState) {

    override fun onCreateMviStore(savedState: Bundle?): MviStore<Intention, State, Event> =
        createMviStore(
            initialState = State(null, null, false),
            bootstrapper = bootstrapper(),
            middleware = mviMiddleware(
                loggingMiddleware(mviLogger),
                mviPostProcessors(sideEffects()),
                TrackSavedPlacesMiddleware(trackSavedPlacesUseCase)
            ),
            reducer = reducer(),
            intentToActionConverter = converter { intent ->
                when (intent) {
                    is Intention.AddPlaceButtonClicked -> Action.AddPlaceButtonClicked
                    is Intention.PlaceClicked -> Action.PlaceClicked(intent.place)
                }
            }
        )

    override fun onSaveMviStoreState(
        mviStore: MviStore<Intention, State, Event>,
        outState: Bundle
    ) {
        // do nothing
    }
}