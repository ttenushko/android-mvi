package com.ttenushko.androidmvi.demo.presentation.screens.home.addplace.mvi

import com.ttenushko.androidmvi.demo.presentation.screens.home.Router
import com.ttenushko.androidmvi.demo.presentation.screens.home.addplace.mvi.AddPlaceStore.Event
import com.ttenushko.androidmvi.demo.presentation.screens.home.addplace.mvi.AddPlaceStore.State
import com.ttenushko.mvi.Dispatcher
import com.ttenushko.mvi.MviPostProcessorMiddleware
import com.ttenushko.mvi.mviPostProcessor

// TODO: check and keep best implementation
internal fun sideEffectsHandler() =
    mviPostProcessor<Action, State, Event> { action, _, _, _, eventDispatcher ->
        when (action) {
            is Action.PlaceSaved -> {
                eventDispatcher.dispatch(Event.Navigation(Router.Destination.GoBack))
            }
            else -> {
                // do nothing
            }
        }
    }

//internal class SideEffects : MviPostProcessorMiddleware.PostProcessor<Action, State, Event> {
//    override fun process(
//        action: Action,
//        oldState: State,
//        newState: State,
//        actionDispatcher: Dispatcher<Action>,
//        eventDispatcher: Dispatcher<Event>
//    ) {
//        when (action) {
//            is Action.PlaceSaved -> {
//                eventDispatcher.dispatch(Event.Navigation(Router.Destination.GoBack))
//            }
//        }
//    }
//}