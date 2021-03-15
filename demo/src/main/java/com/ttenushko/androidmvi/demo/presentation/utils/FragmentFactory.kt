package com.ttenushko.androidmvi.demo.presentation.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import javax.inject.Provider

class FragmentFactory(
    private val providers: Map<Class<out Fragment>, @JvmSuppressWildcards Provider<Fragment>>
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return providers[loadFragmentClass(classLoader, className)]?.get()
            ?: super.instantiate(classLoader, className)
    }
}