package com.ttenushko.mvi

public object MviStores {
    public fun <I, A, S, E> create(
        initialState: S,
        intentToActionConverter: Converter<I, A>? = null,
        bootstrapper: MviBootstrapper<A, S, E>? = null,
        middleware: Collection<MviMiddleware<A, S, E>>? = null,
        reducer: MviReducer<A, S>
    ): MviStore<I, S, E> =
        MviStoreImpl(
            initialState,
            intentToActionConverter,
            bootstrapper,
            middleware,
            reducer
        )
}

public fun <I, A, S, E> createMviStore(
    initialState: S,
    intentToActionConverter: Converter<I, A>? = null,
    bootstrapper: MviBootstrapper<A, S, E>? = null,
    middleware: Collection<MviMiddleware<A, S, E>>? = null,
    reducer: MviReducer<A, S>
): MviStore<I, S, E> =
    MviStoreImpl(
        initialState,
        intentToActionConverter,
        bootstrapper,
        middleware,
        reducer
    )