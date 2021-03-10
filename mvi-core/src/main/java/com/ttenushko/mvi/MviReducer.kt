package com.ttenushko.mvi

public interface MviReducer<A, S> {
    public fun reduce(action: A, state: S): S
}

public fun <A, S> mviReducer(
    reduce: (
        action: A,
        state: S
    ) -> S
): MviReducer<A, S> =
    object : MviReducer<A, S> {
        override fun reduce(action: A, state: S): S =
            reduce(action, state)
    }
