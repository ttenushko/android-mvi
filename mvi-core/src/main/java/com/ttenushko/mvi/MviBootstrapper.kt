package com.ttenushko.mvi


public interface MviBootstrapper<A, S, E> {
    public fun bootstrap(state: S, actionDispatcher: Dispatcher<A>, eventDispatcher: Dispatcher<E>)
}

public fun <A, S, E> mviBootstrapper(
    bootstrapper: (
        state: S,
        actionDispatcher: Dispatcher<A>,
        eventDispatcher: Dispatcher<E>
    ) -> Unit
): MviBootstrapper<A, S, E> =
    object : MviBootstrapper<A, S, E> {
        override fun bootstrap(
            state: S,
            actionDispatcher: Dispatcher<A>,
            eventDispatcher: Dispatcher<E>
        ) {
            bootstrapper(state, actionDispatcher, eventDispatcher)
        }
    }