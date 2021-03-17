package com.ttenushko.androidmvi.demo.presentation.screens.addplace

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.ttenushko.androidmvi.demo.presentation.base.BaseMviFragment
import com.ttenushko.androidmvi.demo.presentation.base.Router
import com.ttenushko.androidmvi.demo.presentation.screens.AppRouter
import com.ttenushko.androidmvi.demo.presentation.screens.addplace.mvi.Store.*
import com.ttenushko.androidmvi.demo.presentation.utils.MviEventLogger
import com.ttenushko.androidmvi.demo.presentation.utils.viewModel
import com.ttenushko.mvi.android.MviStoreViewModel
import kotlinx.coroutines.flow.StateFlow

class AddPlaceFragment(
    private val router: Router<AppRouter.Destination>,
    private val eventLogger: MviEventLogger<Any>,
    private val viewModelProvider: (String, Bundle?) -> MviStoreViewModel<Intention, State, Event>
) : BaseMviFragment<Intention, State, Event>() {

    companion object {
        private const val ARG_SEARCH = "search"

        fun args(search: String): Bundle =
            Bundle().apply {
                putString(ARG_SEARCH, search)
            }
    }

    override fun onMviEvent(event: Event) {
        eventLogger.log(event)
        when (event) {
            is Event.Navigation -> {
                router.navigateTo(event.destination)
            }
        }
    }

    @Composable
    override fun FragmentContent(state: StateFlow<State>) {
        AddPlaceView(
            state = state.collectAsState().value,
            navigationClickHandler = { router.navigateTo(AppRouter.Destination.GoBack) },
            searchChanged = { search -> dispatchMviIntent(Intention.SearchChanged(search)) },
            placeClickHandler = { place -> dispatchMviIntent(Intention.PlaceClicked(place)) }
        )
    }

    override fun getMviStoreViewModel(savedInstanceState: Bundle?): MviStoreViewModel<Intention, State, Event> =
        viewModel {
            viewModelProvider(
                requireArguments().getString(ARG_SEARCH)!!,
                savedInstanceState
            )
        }
}