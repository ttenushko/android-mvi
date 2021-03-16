package com.ttenushko.androidmvi.demo.presentation.screens.addplace

import android.os.Bundle
import com.ttenushko.androidmvi.demo.domain.application.usecase.SavePlaceUseCase
import com.ttenushko.androidmvi.demo.domain.weather.usecase.SearchPlaceUseCase
import com.ttenushko.androidmvi.demo.presentation.screens.addplace.mvi.*
import com.ttenushko.androidmvi.demo.presentation.screens.addplace.mvi.Store.*
import com.ttenushko.androidmvi.demo.presentation.utils.MviLogger
import com.ttenushko.mvi.*
import com.ttenushko.mvi.android.MviStoreViewModel

internal class AddPlacesViewModel(
    private val mviLogger: MviLogger<Action, State>,
    private val search: String,
    private val searchPlaceUseCase: SearchPlaceUseCase,
    private val savePlaceUseCase: SavePlaceUseCase,
    savedState: Bundle?
) : MviStoreViewModel<Intention, State, Event>(savedState) {

    companion object {
        private const val SEARCH = "search"
    }

    override fun onCreateMviStore(savedState: Bundle?): MviStore<Intention, State, Event> {
        val search = savedState?.getString(SEARCH) ?: search
        return createMviStore(
            initialState = State(search, State.SearchResult.Success("", listOf()), false),
            bootstrapper = bootstrapper(),
            middleware = listOf(
                LoggingMiddleware(mviLogger),
                MviPostProcessorMiddleware(listOf(sideEffects())),
                SearchPlaceMiddleware(searchPlaceUseCase),
                AddPlaceMiddleware(savePlaceUseCase)
            ),
            reducer = reducer(),
            intentToActionConverter = converter { intent ->
                when (intent) {
                    is Intention.SearchChanged -> Action.SearchChanged(intent.search)
                    is Intention.PlaceClicked -> Action.PlaceClicked(intent.place)
                }
            }
        )
    }

    override fun onSaveMviStoreState(
        mviStore: MviStore<Intention, State, Event>,
        outState: Bundle
    ) {
        outState.putString(SEARCH, mviStore.state.search)
    }
}