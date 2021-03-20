package com.ttenushko.androidmvi.demo.presentation.screens

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.ttenushko.androidmvi.demo.R
import com.ttenushko.androidmvi.demo.presentation.base.Router
import com.ttenushko.androidmvi.demo.presentation.utils.getActionIdByDestinationId
import com.ttenushko.androidmvi.demo.presentation.screens.addplace.AddPlaceFragment
import com.ttenushko.androidmvi.demo.presentation.screens.placedetails.PlaceDetailsFragment

class AppRouterImpl(
    private val navHostFragment: NavHostFragment
) : Router<AppRouter.Destination> {

    private val navController: NavController by lazy { navHostFragment.navController }

    override fun navigateTo(destination: AppRouter.Destination) {
        val currentDestination =
            navController.currentDestination?.id?.let { navController.graph.findNode(it) }
        when (destination) {
            is AppRouter.Destination.GoBack -> {
                navController.popBackStack()
            }
            is AppRouter.Destination.Places -> {
                val destinationId = R.id.placesFragment
                val actionId =
                    currentDestination?.getActionIdByDestinationId(destinationId) ?: destinationId
                val navOptions = NavOptions.Builder()
                    .setPopUpTo(R.id.nav_graph_main, true)
                    .build()
                navController.navigate(actionId, null, navOptions)
            }
            is AppRouter.Destination.AddPlace -> {
                val destinationId = R.id.addPlaceFragment
                val actionId =
                    currentDestination?.getActionIdByDestinationId(destinationId) ?: destinationId
                navController.navigate(actionId, AddPlaceFragment.args(destination.search))
            }
            is AppRouter.Destination.PlaceDetails -> {
                val destinationId = R.id.placeDetailsFragment
                val actionId =
                    currentDestination?.getActionIdByDestinationId(destinationId) ?: destinationId
                navController.navigate(actionId, PlaceDetailsFragment.args(destination.placeId))
            }
        }
    }
}