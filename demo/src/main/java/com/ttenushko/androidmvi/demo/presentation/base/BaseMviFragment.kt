package com.ttenushko.androidmvi.demo.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.ttenushko.mvi.android.MviStoreViewModel
import kotlinx.coroutines.flow.collect

abstract class BaseMviFragment<I, S, E> : BaseFragment() {

    private lateinit var mviStoreViewModel: MviStoreViewModel<I, S, E>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mviStoreViewModel = getMviStoreViewModel().apply { run() }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        ComposeView(requireContext()).apply {
            setContent {
                content()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    @Composable
    protected abstract fun content()

    protected abstract fun onMviStateChanged(state: S)

    protected abstract fun onMviEvent(event: E)

    protected abstract fun getMviStoreViewModel(): MviStoreViewModel<I, S, E>
}