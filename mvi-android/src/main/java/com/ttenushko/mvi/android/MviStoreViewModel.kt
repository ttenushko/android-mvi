package com.ttenushko.mvi.android

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.ttenushko.mvi.MviStore
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow


@Suppress("EXPERIMENTAL_API_USAGE")
public abstract class MviStoreViewModel<I, S, E>(
    savedState: Bundle?
) : ViewModel() {

    private companion object {
        private const val STATUS_IDLE = 0
        private const val STATUS_RUNNING = 1
        private const val STATUS_CLEARED = 2
    }

    private val lock = Any()
    private var status = STATUS_IDLE
    private lateinit var stateFlow: MutableStateFlow<S>
    private val eventsChannel = BroadcastChannel<E>(Channel.BUFFERED)
    private val mviStore: MviStore<I, S, E>
    private val stateChangedListener = object : MviStore.StateChangedListener<S> {
        override fun onStateChanged(state: S) {
            stateFlow.value = state
        }
    }
    private val eventListener = object : MviStore.EventListener<E> {
        override fun onEvent(event: E) {
            eventsChannel.offer(event)
        }
    }
    public val state: Flow<S> get() = stateFlow
    public val events: Flow<E> = eventsChannel.asFlow()


    init {
        @Suppress("LeakingThis")
        mviStore = onCreateMviStore(savedState).apply {
            addStateChangedListener(stateChangedListener)
            addEventListener(eventListener)
        }
        stateFlow = MutableStateFlow(mviStore.state)
    }

    public fun run() {
        synchronized(lock) {
            when (status) {
                STATUS_IDLE -> {
                    status = STATUS_RUNNING
                    mviStore
                }
                STATUS_RUNNING -> {
                    // ignore multiple run() calls
                    null
                }
                STATUS_CLEARED -> instanceCleared()
                else -> unsupportedStatus(status)
            }
        }?.also { mviStore ->
            mviStore.run()
        }
    }

    final override fun onCleared() {
        super.onCleared()
        synchronized(lock) {
            when (status) {
                STATUS_IDLE -> {
                    status = STATUS_CLEARED
                    null
                }
                STATUS_RUNNING -> {
                    status = STATUS_CLEARED
                    mviStore
                }
                STATUS_CLEARED -> instanceCleared()
                else -> unsupportedStatus(status)
            }
        }?.also { mviStore ->
            mviStore.removeStateChangedListener(stateChangedListener)
            mviStore.removeEventListener(eventListener)
            mviStore.close()
        }
    }

    public fun dispatchIntent(intent: I) {
        synchronized(lock) {
            when (status) {
                STATUS_IDLE -> notRunning()
                STATUS_RUNNING -> mviStore
                STATUS_CLEARED -> instanceCleared()
                else -> unsupportedStatus(status)
            }
        }.also { mviStore ->
            mviStore.dispatchIntent(intent)
        }
    }

    public fun saveState(outState: Bundle) {
        synchronized(lock) {
            when (status) {
                STATUS_IDLE -> notRunning()
                STATUS_RUNNING -> Unit
                STATUS_CLEARED -> instanceCleared()
                else -> unsupportedStatus(status)
            }
        }.also {
            onSaveMviStoreState(mviStore, outState)
        }
    }

    protected abstract fun onCreateMviStore(savedState: Bundle?): MviStore<I, S, E>

    protected abstract fun onSaveMviStoreState(mviStore: MviStore<I, S, E>, outState: Bundle)

    private fun notRunning(): Nothing =
        throw IllegalStateException("This instance is not running yet. Call 'run()' first.")

    private fun instanceCleared(): Nothing =
        throw IllegalStateException("This instance is cleared.")

    private fun unsupportedStatus(status: Int): Nothing =
        throw throw IllegalStateException("Unsupported status: $status.")
}