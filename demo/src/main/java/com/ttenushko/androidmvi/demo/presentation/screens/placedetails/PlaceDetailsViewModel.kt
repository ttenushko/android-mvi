package com.ttenushko.androidmvi.demo.presentation.screens.placedetails

import android.os.Bundle
import com.ttenushko.androidmvi.demo.domain.application.usecase.DeletePlaceUseCase
import com.ttenushko.androidmvi.demo.domain.weather.usecase.GetCurrentWeatherConditionsUseCase
import com.ttenushko.androidmvi.demo.presentation.screens.placedetails.mvi.*
import com.ttenushko.androidmvi.demo.presentation.screens.placedetails.mvi.Store.*
import com.ttenushko.androidmvi.demo.presentation.utils.MviLogger
import com.ttenushko.mvi.*
import com.ttenushko.mvi.android.MviStoreViewModel

internal class PlaceDetailsViewModel(
    private val mviLogger: MviLogger<Action, State>,
    private val placeId: Long,
    private val getCurrentWeatherConditionsUseCase: GetCurrentWeatherConditionsUseCase,
    private val deletePlaceUseCase: DeletePlaceUseCase,
    savedState: Bundle?
) : MviStoreViewModel<Intention, State, Event>(savedState) {

    override fun onCreateMviStore(savedState: Bundle?): MviStore<Intention, State, Event> =
        MviStores.create(
            initialState = State(placeId, null, null, false, false),
            bootstrapper = bootstrapper(),
            middleware = listOf(
                LoggingMiddleware(mviLogger),
                mviPostProcessors(sideEffects()),
                GetCurrentWeatherMiddleware(getCurrentWeatherConditionsUseCase),
                DeletePlaceMiddleware(deletePlaceUseCase)
            ),
            reducer = reducer(),
            intentToActionConverter = Converter { intent ->
                when (intent) {
                    is Intention.DeleteClicked -> Action.DeleteClicked
                    is Intention.DeleteConfirmed -> Action.Delete
                    is Intention.Refresh -> Action.Reload
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