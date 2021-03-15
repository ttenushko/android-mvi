package com.ttenushko.androidmvi.demo.di.framework.android

import android.content.Context
import com.ttenushko.androidmvi.demo.di.PublicDependencies
import com.ttenushko.androidmvi.demo.di.data.DataModule
import com.ttenushko.androidmvi.demo.framework.android.App
import com.ttenushko.androidmvi.demo.presentation.utils.dagger.ApplicationContext
import com.ttenushko.androidmvi.demo.presentation.utils.dagger.ApplicationScope
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        AppModule::class,
        DataModule::class,
        AppDependenciesProviderModule::class
    ]
)
@ApplicationScope
interface AppComponent : PublicDependencies {

    @Component.Builder
    interface Builder {
        @BindsInstance
        @ApplicationContext
        fun applicationContext(applicationContext: Context): Builder
        fun appModule(module: AppModule): Builder
        fun dataModule(module: DataModule): Builder
        fun build(): AppComponent
    }

    fun inject(app: App)
}

