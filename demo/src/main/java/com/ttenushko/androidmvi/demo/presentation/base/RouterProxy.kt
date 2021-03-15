package com.ttenushko.androidmvi.demo.presentation.base

class RouterProxy<D : Router.Destination> : Router<D> {
    private var router: Router<D>? = null
    private val pendingDestinations = mutableListOf<D>()

    override fun navigateTo(destination: D) {
        router?.navigateTo(destination) ?: pendingDestinations.add(destination)
    }

    fun attach(router: Router<D>) {
        require(null == this.router) { "Router already attached" }
        this.router = router
        pendingDestinations.removeAll { destination ->
            router.navigateTo(destination)
            true
        }
    }

    fun detach(router: Router<D>) {
        require(router === this.router) { "Trying to detach incorrect router" }
        this.router = null
    }
}

fun <D : Router.Destination> Router<D>.asProxy(): RouterProxy<D>? =
    this as? RouterProxy<D>