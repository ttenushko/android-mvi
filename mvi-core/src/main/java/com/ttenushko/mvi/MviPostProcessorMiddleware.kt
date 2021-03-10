package com.ttenushko.mvi

public class MviPostProcessorMiddleware<A, S, E>(
    private val postProcessors: List<PostProcessor<A, S, E>>
) : MviMiddleware<A, S, E> {

    private val closeHandler = CloseHandler {
        // do nothing
    }

    override fun apply(chain: MviMiddleware.Chain<A, S, E>) {
        closeHandler.checkNotClosed()
        val actionDispatcher = chain.actionDispatcher
        val eventDispatcher = chain.eventDispatcher
        val action = chain.action
        val oldState = chain.state
        val newState = chain.proceed().let { chain.state }
        postProcessors.forEach {
            it.process(
                action,
                oldState,
                newState,
                actionDispatcher,
                eventDispatcher
            )
        }
    }

    override fun close() {
        closeHandler.close()
    }

    public interface PostProcessor<A, S, E> {
        public fun process(
            action: A,
            oldState: S,
            newState: S,
            actionDispatcher: Dispatcher<A>,
            eventDispatcher: Dispatcher<E>
        )
    }
}

public fun <A, S, E> mviPostProcessor(
    postProcessor: (
        action: A,
        oldState: S,
        newState: S,
        actionDispatcher: Dispatcher<A>,
        eventDispatcher: Dispatcher<E>
    ) -> Unit
): MviPostProcessorMiddleware.PostProcessor<A, S, E> =
    object : MviPostProcessorMiddleware.PostProcessor<A, S, E> {
        override fun process(
            action: A,
            oldState: S,
            newState: S,
            actionDispatcher: Dispatcher<A>,
            eventDispatcher: Dispatcher<E>
        ) {
            postProcessor(action, oldState, newState, actionDispatcher, eventDispatcher)
        }
    }

public fun <A, S, E> mviPostProcessors(vararg postProcessors: MviPostProcessorMiddleware.PostProcessor<A, S, E>): MviPostProcessorMiddleware<A, S, E> =
    MviPostProcessorMiddleware(listOf(*postProcessors))
