package com.ttenushko.androidmvi.demo.presentation.screens.home.addplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ttenushko.androidmvi.demo.R
import com.ttenushko.androidmvi.demo.databinding.FragmentAddPlaceBinding
import com.ttenushko.androidmvi.demo.domain.weather.model.Place
import com.ttenushko.androidmvi.demo.presentation.base.BaseMviFragment
import com.ttenushko.androidmvi.demo.presentation.di.utils.findComponentDependencies
import com.ttenushko.androidmvi.demo.presentation.screens.home.Router
import com.ttenushko.androidmvi.demo.presentation.screens.home.addplace.di.AddPlaceFragmentModule
import com.ttenushko.androidmvi.demo.presentation.screens.home.addplace.di.DaggerAddPlaceFragmentComponent
import com.ttenushko.androidmvi.demo.presentation.screens.home.addplace.mvi.AddPlaceStore.*
import com.ttenushko.androidmvi.demo.presentation.screens.home.common.PlaceAdapter
import com.ttenushko.androidmvi.demo.presentation.utils.MviEventLogger
import com.ttenushko.androidmvi.demo.presentation.utils.isVisible
import com.ttenushko.mvi.android.MviStoreViewModel
import javax.inject.Inject


class AddPlaceFragment :
    BaseMviFragment<Intention, State, Event>() {

    companion object {
        private const val ARG_SEARCH = "search"
        fun args(search: String): Bundle =
            Bundle().apply {
                putString(ARG_SEARCH, search)
            }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var eventLogger: MviEventLogger<Any>
    private var placeAdapter: PlaceAdapter? = null
    private var _viewBinding: FragmentAddPlaceBinding? = null
    private val viewBinding
        get() = _viewBinding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerAddPlaceFragmentComponent.builder()
            .addPlaceFragmentDependencies(findComponentDependencies())
            .addPlaceFragmentModule(
                AddPlaceFragmentModule(
                    arguments!!.getString(ARG_SEARCH)!!,
                    savedInstanceState
                )
            )
            .build()
            .inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentAddPlaceBinding.inflate(inflater, container, false).also {
            _viewBinding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        placeAdapter = PlaceAdapter(
            context!!,
            object :
                PlaceAdapter.Callback {
                override fun onItemClicked(place: Place) {
                    dispatchMviIntent(Intention.PlaceClicked(place))
                }
            })
        viewBinding.placeList.apply {
            adapter = placeAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        viewBinding.toolbar.searchView.apply {
            isFocusable = true
            isIconified = false
            setOnQueryTextListener(searchTextWatcher)
            setIconifiedByDefault(false)
            requestFocusFromTouch()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding.toolbar.searchView.setOnQueryTextListener(null)
        placeAdapter = null
        _viewBinding = null
    }

    override fun onStart() {
        super.onStart()
        toolbar?.title = "Add Place"
    }

    override fun onMviStateChanged(state: State) {
        if (state.search != viewBinding.toolbar.searchView.query.toString()) {
            viewBinding.toolbar.searchView.setOnQueryTextListener(null)
            viewBinding.toolbar.searchView.setQuery(state.search, true)
            viewBinding.toolbar.searchView.setOnQueryTextListener(searchTextWatcher)
        }
        when (val searchResult = state.searchResult) {
            is State.SearchResult.Success -> {
                placeAdapter!!.set(searchResult.places)
            }
            is State.SearchResult.Failure -> {
                placeAdapter!!.clear()
            }
        }
        viewBinding.progress.isVisible = state.isSearching
        when {
            state.isShowSearchPrompt -> {
                viewBinding.message.isVisible = true
                viewBinding.message.text = "Start typing text to search"
            }
            state.isShowSearchNoResultsPrompt -> {
                viewBinding.message.isVisible = true
                viewBinding.message.text = "Nothing found"
            }
            state.isShowSearchErrorPrompt -> {
                viewBinding.message.isVisible = true
                viewBinding.message.text = "Error occurred"
            }
            else -> {
                viewBinding.message.isVisible = false
            }
        }
    }

    override fun onMviEvent(event: Event) {
        eventLogger.log(event)
        when (event) {
            is Event.Navigation -> {
                getTarget(Router::class.java)?.navigateTo(event.destination)
            }
        }
    }

    override fun getMviStoreViewModel(): MviStoreViewModel<Intention, State, Event> =
        ViewModelProvider(this, viewModelFactory).get(AddPlacesFragmentViewModel::class.java)

    private val searchTextWatcher = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(text: String): Boolean {
            return false
        }

        override fun onQueryTextChange(text: String): Boolean {
            dispatchMviIntent(Intention.SearchChanged(text))
            return true
        }
    }
}