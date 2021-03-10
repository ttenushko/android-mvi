package com.ttenushko.mvi


public class LoggingMiddleware<A, S, E>(
    private val logger: Logger<A, S>
) : MviMiddleware<A, S, E> {

    private val closeHandler = CloseHandler {
        // do nothing
    }

    override fun apply(chain: MviMiddleware.Chain<A, S, E>) {
        closeHandler.checkNotClosed()
        val action = chain.action
        val oldState = chain.state
        val newState = chain.proceed().let { chain.state }
        logger.log(action, oldState, newState)
    }

    override fun close() {
        closeHandler.close()
    }

    public interface Logger<A, S> {
        public fun log(action: A, oldState: S, newState: S)
    }
}