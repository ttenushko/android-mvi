package com.ttenushko.mvi

import java.io.Closeable

public interface MviStore<I, S, E> : Closeable {
    public val state: S

    public fun run()
    public fun dispatchIntent(intent: I)
    public fun addStateChangedListener(listener: StateChangedListener)
    public fun removeStateChangedListener(listener: StateChangedListener)
    public fun addEventListener(listener: EventListener<E>)
    public fun removeEventListener(listener: EventListener<E>)

    public interface StateChangedListener {
        public fun onStateChanged()
    }

    public interface EventListener<E> {
        public fun onEvent(event: E)
    }
}