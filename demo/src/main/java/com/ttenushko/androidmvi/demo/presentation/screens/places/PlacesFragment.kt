package com.ttenushko.androidmvi.demo.presentation.screens.places

import android.os.Bundle
import androidx.compose.runtime.Composable
import com.ttenushko.androidmvi.demo.presentation.base.BaseMviFragment
import com.ttenushko.androidmvi.demo.presentation.base.Router
import com.ttenushko.androidmvi.demo.presentation.screens.AppRouter
import com.ttenushko.androidmvi.demo.presentation.screens.places.mvi.Store.*
import com.ttenushko.androidmvi.demo.presentation.utils.MviEventLogger
import com.ttenushko.androidmvi.demo.presentation.utils.viewModel
import com.ttenushko.mvi.android.MviStoreViewModel

class PlacesFragment(
    private val router: Router<AppRouter.Destination>,
    private val eventLogger: MviEventLogger<Any>,
    private val viewModelProvider: (Bundle?) -> MviStoreViewModel<Intention, State, Event>
) : BaseMviFragment<Intention, State, Event>() {

    override fun onMviStateChanged(state: State) {
        // TODO: implement me
    }

    override fun onMviEvent(event: Event) {
        eventLogger.log(event)
        // TODO: implement me
    }

    override fun getMviStoreViewModel(savedInstanceState: Bundle?): MviStoreViewModel<Intention, State, Event> =
        viewModel { viewModelProvider(savedInstanceState) }

    @Composable
    override fun FragmentContent() {
        // TODO: implement me
    }
}
