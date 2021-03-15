package com.ttenushko.androidmvi.demo.presentation.base

interface Router<D : Router.Destination> {
    fun navigateTo(destination: D)

    interface Destination
}