package com.ttenushko.androidmvi.demo.presentation.screens.placedetails

import android.content.DialogInterface
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.fragment.app.DialogFragment
import com.squareup.picasso.Picasso
import com.ttenushko.androidmvi.demo.presentation.base.BaseMviFragment
import com.ttenushko.androidmvi.demo.presentation.base.DefaultErrorHandler
import com.ttenushko.androidmvi.demo.presentation.base.Router
import com.ttenushko.androidmvi.demo.presentation.dialog.DialogFragmentClickListener
import com.ttenushko.androidmvi.demo.presentation.dialog.SimpleDialogFragment
import com.ttenushko.androidmvi.demo.presentation.screens.AppRouter
import com.ttenushko.androidmvi.demo.presentation.screens.placedetails.mvi.Store.*
import com.ttenushko.androidmvi.demo.presentation.utils.MviEventLogger
import com.ttenushko.androidmvi.demo.presentation.utils.isDialogShown
import com.ttenushko.androidmvi.demo.presentation.utils.showDialog
import com.ttenushko.androidmvi.demo.presentation.utils.viewModel
import com.ttenushko.mvi.android.MviStoreViewModel
import kotlinx.coroutines.flow.StateFlow

class PlaceDetailsFragment(
    private val router: Router<AppRouter.Destination>,
    private val eventLogger: MviEventLogger<Any>,
    private val picasso: Picasso,
    private val viewModelProvider: (Long, Bundle?) -> MviStoreViewModel<Intention, State, Event>
) : BaseMviFragment<Intention, State, Event>(), DialogFragmentClickListener {

    companion object {
        private const val ARG_PLACE_ID = "placeId"
        private const val DLG_DELETE_CONFIRMATION = "deleteConfirmation"

        fun args(placeId: Long): Bundle =
            Bundle().apply {
                putLong(ARG_PLACE_ID, placeId)
            }
    }

    override fun onMviEvent(event: Event) {
        eventLogger.log(event)
        when (event) {
            is Event.Navigation -> {
                router.navigateTo(event.destination)
            }
            is Event.ShowDeleteConfirmation -> {
                if (!childFragmentManager.isDialogShown(DLG_DELETE_CONFIRMATION)) {
                    childFragmentManager.showDialog(
                        SimpleDialogFragment.newInstance(
                            null,
                            "Are you sure to remove this place?",
                            "OK",
                            "Cancel"
                        ), DLG_DELETE_CONFIRMATION
                    )
                }
            }
            is Event.ShowError -> {
                DefaultErrorHandler.showError(this, null, event.error)
            }
        }
    }

    override fun onDialogFragmentClick(
        dialogFragment: DialogFragment,
        dialog: DialogInterface,
        which: Int
    ) {
        when (dialogFragment.tag) {
            DLG_DELETE_CONFIRMATION -> {
                if (DialogInterface.BUTTON_POSITIVE == which) {
                    dispatchMviIntent(Intention.DeleteConfirmed)
                }
            }
        }
    }

    override fun getMviStoreViewModel(savedInstanceState: Bundle?): MviStoreViewModel<Intention, State, Event> =
        viewModel {
            viewModelProvider(
                requireArguments().getLong(ARG_PLACE_ID),
                savedInstanceState
            )
        }

    @Composable
    override fun FragmentContent(state: StateFlow<State>) {
        PlaceDetailsView(
            state = state.collectAsState().value,
            navigationClickHandler = { router.navigateTo(AppRouter.Destination.GoBack) },
            deleteClickHandler = { dispatchMviIntent(Intention.DeleteClicked) },
        )
    }
}