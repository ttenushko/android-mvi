package com.ttenushko.androidmvi.demo.presentation.base

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.ttenushko.mvi.android.MviStoreViewModel
import kotlinx.coroutines.flow.collect

abstract class BaseMviFragment<I, S, E> : BaseFragment() {

    private lateinit var mviStoreViewModel: MviStoreViewModel<I, S, E>
    private val mviStoreStateObserver = Observer<S> { onMviStateChanged(it) }
    private val mviStoreEventObserver = Observer<E> { onMviEvent(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mviStoreViewModel = getMviStoreViewModel(savedInstanceState).apply { run() }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            mviStoreViewModel.state.collect { onMviStateChanged(it) }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            mviStoreViewModel.events.collect { onMviEvent(it) }
        }
    }
    
    override fun onSaveInstanceState(outState: Bundle) {
        mviStoreViewModel.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    protected fun dispatchMviIntent(intent: I) {
        mviStoreViewModel.dispatchIntent(intent)
    }

    protected abstract fun onMviStateChanged(state: S)

    protected abstract fun onMviEvent(event: E)

    protected abstract fun getMviStoreViewModel(savedState: Bundle?): MviStoreViewModel<I, S, E>
}