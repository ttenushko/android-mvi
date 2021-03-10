package com.ttenushko.mvi

import java.io.Closeable

public interface MviMiddleware<A, S, E> : Closeable {
    public fun apply(chain: Chain<A, S, E>)

    public interface Chain<A, S, E> {
        public val action: A
        public val state: S
        public val actionDispatcher: Dispatcher<A>
        public val eventDispatcher: Dispatcher<E>

        public fun proceed()
    }
}