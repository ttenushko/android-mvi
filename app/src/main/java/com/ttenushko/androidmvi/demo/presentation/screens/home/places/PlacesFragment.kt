package com.ttenushko.androidmvi.demo.presentation.screens.home.places

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ttenushko.androidmvi.demo.R
import com.ttenushko.androidmvi.demo.databinding.FragmentAddPlaceBinding
import com.ttenushko.androidmvi.demo.databinding.FragmentPlacesBinding
import com.ttenushko.androidmvi.demo.domain.weather.model.Place
import com.ttenushko.androidmvi.demo.presentation.base.BaseMviFragment
import com.ttenushko.androidmvi.demo.presentation.di.utils.findComponentDependencies
import com.ttenushko.androidmvi.demo.presentation.screens.home.Router
import com.ttenushko.androidmvi.demo.presentation.screens.home.common.PlaceAdapter
import com.ttenushko.androidmvi.demo.presentation.screens.home.places.di.DaggerPlacesFragmentComponent
import com.ttenushko.androidmvi.demo.presentation.screens.home.places.di.PlacesFragmentModule
import com.ttenushko.androidmvi.demo.presentation.screens.home.places.mvi.Store.*
import com.ttenushko.androidmvi.demo.presentation.utils.MviEventLogger
import com.ttenushko.androidmvi.demo.presentation.utils.isVisible
import com.ttenushko.mvi.android.MviStoreViewModel
import javax.inject.Inject

class PlacesFragment :
    BaseMviFragment<Intention, State, Event>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var eventLogger: MviEventLogger<Any>
    private var placeAdapter: PlaceAdapter? = null
    private var _viewBinding: FragmentPlacesBinding? = null
    private val viewBinding
        get() = _viewBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerPlacesFragmentComponent.builder()
            .placesFragmentDependencies(findComponentDependencies())
            .placesFragmentModule(PlacesFragmentModule(savedInstanceState))
            .build()
            .inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        FragmentPlacesBinding.inflate(inflater, container, false).also {
            _viewBinding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.layoutContent.btnAddPlace.setOnClickListener {
            dispatchMviIntent(Intention.AddPlaceButtonClicked)
        }
        placeAdapter = PlaceAdapter(
            context!!,
            object :
                PlaceAdapter.Callback {
                override fun onItemClicked(place: Place) {
                    dispatchMviIntent(Intention.PlaceClicked(place))
                }
            })
        viewBinding.layoutContent.placeList.adapter = placeAdapter
        viewBinding.layoutContent.placeList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        placeAdapter = null
        _viewBinding = null
    }

    override fun onStart() {
        super.onStart()
        toolbar?.title = "Places"
    }

    override fun onMviStateChanged(state: State) {
        viewBinding.layoutContent.root.isVisible = (null != state.places)
        viewBinding.layoutContent.layoutContentFilled.isVisible = (null != state.places && state.places.isNotEmpty())
        viewBinding.layoutContent.layoutContentEmpty.isVisible = (null != state.places && state.places.isEmpty())
        viewBinding.layoutError.root.isVisible = (null != state.error)
        viewBinding.layoutLoading.root.isVisible = state.isLoading
        if (null != state.places) {
            placeAdapter!!.set(state.places)
        } else {
            placeAdapter!!.clear()
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
        ViewModelProvider(this, viewModelFactory).get(PlacesFragmentViewModel::class.java)
}
