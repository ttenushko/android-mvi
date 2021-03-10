package com.ttenushko.mvi


public abstract class MviMiddlewareImpl<A, S, E> :
    MviMiddleware<A, S, E> {

    private lateinit var _actionDispatcher: Dispatcher<A>
    private lateinit var _eventDispatcher: Dispatcher<E>
    protected val actionDispatcher: Dispatcher<A>
        get() = _actionDispatcher
    protected val eventDispatcher: Dispatcher<E>
        get() = _eventDispatcher
    private val closeHandler = CloseHandler {
        onClose()
    }

    final override fun apply(chain: MviMiddleware.Chain<A, S, E>) {
        closeHandler.checkNotClosed()
        when {
            !::_actionDispatcher.isInitialized -> _actionDispatcher = chain.actionDispatcher
            _actionDispatcher != chain.actionDispatcher -> throw IllegalArgumentException("Action dispatcher changed across MviMiddleware.apply() calls.")
        }
        when {
            !::_eventDispatcher.isInitialized -> _eventDispatcher = chain.eventDispatcher
            _eventDispatcher != chain.eventDispatcher -> throw IllegalArgumentException("Event dispatcher changed across MviMiddleware.apply() calls.")
        }
        onApply(chain)
    }

    final override fun close() {
        closeHandler.close()
    }

    protected abstract fun onApply(chain: MviMiddleware.Chain<A, S, E>)

    protected abstract fun onClose()
}