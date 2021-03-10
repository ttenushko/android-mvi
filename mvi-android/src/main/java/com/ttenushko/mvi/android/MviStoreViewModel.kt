package com.ttenushko.mvi.android

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ttenushko.mvi.MviStore
import kotlinx.coroutines.flow.MutableStateFlow


public class MviStoreViewModel<I, S, E>(
    savedState: Bundle?,
    private val callback: Callback<I, S, E>,
) : ViewModel() {

    private companion object {
        private const val STATUS_IDLE = 0
        private const val STATUS_RUNNING = 1
        private const val STATUS_CLEARED = 2
    }

    private var lock = Any()
    private var status = STATUS_IDLE
    private val stateFlow: MutableStateFlow<S>
    private val eventsLiveData = MutableLiveDataQueue<E>()
    private val mviStore: MviStore<I, S, E>
    private val stateChangedListener =
        object : MviStore.StateChangedListener<S> {
            override fun onStateChanged(state: S) {
                stateLiveData.postValue(state)
            }
        }
    private val eventListener = object : MviStore.EventListener<E> {
        override fun onEvent(event: E) {
            eventsLiveData.postValue(event)
        }

    }
    public val state: LiveData<S>
        get() = stateLiveData
    public val events: LiveData<E>
        get() = eventsLiveData

    init {
        mviStore = callback.onCreateMviStore(savedState).apply {
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

    override fun onCleared() {
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
                STATUS_RUNNING -> callback
                STATUS_CLEARED -> instanceCleared()
                else -> unsupportedStatus(status)
            }
        }.also { callback ->
            callback.onSaveMviStoreState(mviStore, outState)
        }
    }

    private fun notRunning(): Nothing =
        throw IllegalStateException("This instance is not running yet. Call 'run()' first.")

    private fun instanceCleared(): Nothing =
        throw IllegalStateException("This instance is cleared.")

    private fun unsupportedStatus(status: Int): Nothing =
        throw throw IllegalStateException("Unsupported status: $status.")

    public interface Callback<I, S, E> {
        public fun onCreateMviStore(savedState: Bundle?): MviStore<I, S, E>
        public fun onSaveMviStoreState(
            mviStore: MviStore<I, S, E>,
            outState: Bundle?
        ): MviStore<I, S, E>
    }
}