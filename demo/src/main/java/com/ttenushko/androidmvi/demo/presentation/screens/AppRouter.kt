package com.ttenushko.androidmvi.demo.presentation.screens

import com.ttenushko.androidmvi.demo.presentation.base.Router


interface AppRouter {

    sealed class Destination : Router.Destination {
        object GoBack : Destination() {
            override fun toString(): String =
                "GoBack"
        }

        object Places : Destination() {
            override fun toString(): String =
                "Places"
        }

        data class AddPlace(val search: String) : Destination()
        data class PlaceDetails(val placeId: Long) : Destination()
    }
}