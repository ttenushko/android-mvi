package com.ttenushko.androidmvi.demo.presentation.screens.home.addplace

import android.os.Bundle
import com.ttenushko.androidmvi.demo.domain.application.usecase.SavePlaceUseCase
import com.ttenushko.androidmvi.demo.domain.weather.usecase.SearchPlaceUseCase
import com.ttenushko.androidmvi.demo.presentation.screens.home.addplace.mvi.*
import com.ttenushko.androidmvi.demo.presentation.screens.home.addplace.mvi.AddPlaceStore.*
import com.ttenushko.androidmvi.demo.presentation.utils.MviLogger
import com.ttenushko.mvi.*
import com.ttenushko.mvi.android.MviStoreViewModel

internal class AddPlacesFragmentViewModel(
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
            bootstrapper = Bootstrapper(),
            middleware = listOf(
                LoggingMiddleware(mviLogger),
                mviPostProcessors(sideEffectsHandler()),
                SearchPlaceMiddleware(searchPlaceUseCase),
                AddPlaceMiddleware(savePlaceUseCase)
            ),
            reducer = reducer(),
            intentToActionConverter = Converter { intent ->
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