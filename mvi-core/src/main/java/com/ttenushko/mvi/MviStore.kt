package com.ttenushko.mvi

import java.io.Closeable

public interface MviStore<I, S, E> : Closeable {
    public val state: S

    public fun run()
    public fun dispatchIntent(intent: I)
    public fun addStateChangedListener(listener: StateChangedListener<S>)
    public fun removeStateChangedListener(listener: StateChangedListener<S>)
    public fun addEventListener(listener: EventListener<E>)
    public fun removeEventListener(listener: EventListener<E>)

    public interface StateChangedListener<S> {
        public fun onStateChanged(state: S)
    }

    public interface EventListener<E> {
        public fun onEvent(event: E)
    }
}