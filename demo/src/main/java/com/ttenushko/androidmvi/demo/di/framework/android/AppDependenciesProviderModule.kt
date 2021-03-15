package com.ttenushko.androidmvi.demo.di.framework.android

import com.ttenushko.androidmvi.demo.di.Dependency
import com.ttenushko.androidmvi.demo.di.DependencyKey
import com.ttenushko.androidmvi.demo.di.PublicDependencies
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AppDependenciesProviderModule {
    @Binds
    @IntoMap
    @DependencyKey(PublicDependencies::class)
    abstract fun publicComponents(component: AppComponent): Dependency
}