package com.ttenushko.androidmvi.demo.framework.android

import android.app.Application
import android.content.Context
import com.ttenushko.androidmvi.demo.Config
import com.ttenushko.androidmvi.demo.di.DependenciesProvider
import com.ttenushko.androidmvi.demo.di.ProvidesDependencies
import com.ttenushko.androidmvi.demo.di.data.DataModule
import com.ttenushko.androidmvi.demo.di.framework.android.AppModule
import com.ttenushko.androidmvi.demo.di.framework.android.DaggerAppComponent
import javax.inject.Inject

class App : Application(), ProvidesDependencies {

    companion object {
        lateinit var instance: App
            private set
    }

    @Suppress("ProtectedInFinal")
    @Inject
    override lateinit var dependenciesProvider: DependenciesProvider
        protected set

    override fun onCreate() {
        instance = this
        super.onCreate()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        DaggerAppComponent.builder()
            .applicationContext(this)
            .appModule(AppModule(this, Config.IS_DEBUG))
            .dataModule(DataModule(Config.IS_DEBUG))
            .build()
            .inject(this)
    }
}