package com.ttenushko.mvi


public interface MviBootstrapper<A, S, E> {
    public fun bootstrap(state: S, actionDispatcher: Dispatcher<A>, eventDispatcher: Dispatcher<E>)
}
