package com.ttenushko.androidmvi.demo.presentation.screens.home.placedetails

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import com.ttenushko.androidmvi.MviStoreViewModel
import com.ttenushko.androidmvi.demo.R
import com.ttenushko.androidmvi.demo.presentation.base.BaseMviFragment
import com.ttenushko.androidmvi.demo.presentation.base.DefaultErrorHandler
import com.ttenushko.androidmvi.demo.presentation.di.utils.findComponentDependencies
import com.ttenushko.androidmvi.demo.presentation.dialogs.DialogFragmentClickListener
import com.ttenushko.androidmvi.demo.presentation.dialogs.SimpleDialogFragment
import com.ttenushko.androidmvi.demo.presentation.screens.home.Router
import com.ttenushko.androidmvi.demo.presentation.screens.home.placedetails.di.DaggerPlaceDetailsFragmentComponent
import com.ttenushko.androidmvi.demo.presentation.screens.home.placedetails.di.PlaceDetailsFragmentModule
import com.ttenushko.androidmvi.demo.presentation.screens.home.placedetails.mvi.PlaceDetailsStore.*
import com.ttenushko.androidmvi.demo.presentation.utils.MviEventLogger
import com.ttenushko.androidmvi.demo.presentation.utils.ValueUpdater
import com.ttenushko.androidmvi.demo.presentation.utils.isDialogShown
import com.ttenushko.androidmvi.demo.presentation.utils.showDialog
import kotlinx.android.synthetic.main.fragment_place_details.*
import javax.inject.Inject

class PlaceDetailsFragment :
    BaseMviFragment<Intention, State, Event>(), DialogFragmentClickListener {

    companion object {
        private const val ARG_PLACE_ID = "placeId"
        private const val DLG_DELETE_CONFIRMATION = "deleteConfirmation"

        fun args(placeId: Long): Bundle =
            Bundle().apply {
                putLong(ARG_PLACE_ID, placeId)
            }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var picasso: Picasso
    @Inject
    lateinit var eventLogger: MviEventLogger<Any>
    private var menuItemDelete: MenuItem? = null
    private val menuItemDeleteVisibilityUpdater = ValueUpdater(false) { isVisible ->
        menuItemDelete?.isVisible = isVisible
    }
    private val weatherIconUrl = ValueUpdater("") { iconUrl ->
        if (iconUrl.isNotBlank()) {
            picasso.load(iconUrl).into(icon)
        } else {
            icon.setImageBitmap(null)
        }
    }

    init {
        setHasOptionsMenu(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerPlaceDetailsFragmentComponent.builder()
            .placeDetailsFragmentDependencies(findComponentDependencies())
            .placeDetailsFragmentModule(PlaceDetailsFragmentModule(arguments!!.getLong(ARG_PLACE_ID)))
            .build()
            .inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_place_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherIconUrl.set("")
        pullRefreshLayout.setOnRefreshListener { dispatchMviIntent(Intention.Refresh) }
    }

    override fun onStart() {
        super.onStart()
        toolbar?.title = "Place Details"
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_place_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
        menuItemDelete = menu.findItem(R.id.delete)
        menuItemDeleteVisibilityUpdater.forceUpdate()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.delete -> {
                dispatchMviIntent(Intention.DeleteClicked)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }

    override fun onMviStateChanged(state: State) {
        when {
            null != state.error -> {
                layoutContent.visibility = View.GONE
                layoutError.visibility = View.VISIBLE
            }
            null != state.weather -> {
                layoutContent.visibility = View.VISIBLE
                layoutError.visibility = View.GONE
                place.text =
                    "${state.weather.place.name}, ${state.weather.place.countyCode.toUpperCase()}"
                temperature.text = "${state.weather.conditions.tempCurrent.toInt()}\u2103"
                temp_min.text = "${state.weather.conditions.tempMin.toInt()}\u2103"
                temp_max.text = "${state.weather.conditions.tempMax.toInt()}\u2103"
                humidity.text = "${state.weather.conditions.humidity}%"
            }
            else -> {
                layoutContent.visibility = View.GONE
                layoutError.visibility = View.GONE
            }
        }
        pullRefreshLayout.isRefreshing = state.isLoading
        menuItemDeleteVisibilityUpdater.set(state.isDeleteButtonVisible)
        layoutProgress.visibility = if (state.isDeleting) View.VISIBLE else View.GONE
        weatherIconUrl.set(state.weather?.descriptions?.firstOrNull()?.iconUrl ?: "")
    }

    override fun onMviEvent(event: Event) {
        eventLogger.log(event)
        when (event) {
            is Event.Navigation -> {
                getTarget(Router::class.java)?.navigateTo(event.destination)
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

    @Suppress("UNCHECKED_CAST")
    override fun getMviStoreViewModel(): MviStoreViewModel<Intention, State, Event> =
        ViewModelProviders.of(this, viewModelFactory)[PlaceDetailsFragmentViewModel::class.java]
}