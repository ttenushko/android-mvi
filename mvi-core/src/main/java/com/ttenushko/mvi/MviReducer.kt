package com.ttenushko.mvi

public fun interface MviReducer<A, S> {
    public fun reduce(action: A, state: S): S
}
