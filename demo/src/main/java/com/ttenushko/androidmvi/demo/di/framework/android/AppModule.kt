package com.ttenushko.androidmvi.demo.di.framework.android

import android.app.Application
import android.content.Context
import com.squareup.picasso.Picasso
import com.ttenushko.androidmvi.demo.presentation.base.Router
import com.ttenushko.androidmvi.demo.presentation.base.RouterProxy
import com.ttenushko.androidmvi.demo.presentation.screens.AppRouter
import com.ttenushko.androidmvi.demo.presentation.utils.MviEventLogger
import com.ttenushko.androidmvi.demo.presentation.utils.MviLogger
import com.ttenushko.androidmvi.demo.presentation.utils.dagger.ApplicationContext
import com.ttenushko.androidmvi.demo.presentation.utils.dagger.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class AppModule(
    private val application: Application,
    private val isDebug: Boolean
) {
    @ApplicationScope
    @Provides
    fun picasso(): Picasso {
        val builder = Picasso.Builder(application.applicationContext)
        if (isDebug) {
            builder.loggingEnabled(true)
            //builder.indicatorsEnabled(true)
        }
        return builder.build()
    }

    @ApplicationScope
    @Provides
    fun mviEventLogger(): MviEventLogger<Any> =
        MviEventLogger("_mvi_")

    @ApplicationScope
    @Provides
    fun mviLogger(): MviLogger<Any, Any> =
        MviLogger("_mvi_")

    @ApplicationScope
    @Provides
    fun router(): Router<AppRouter.Destination> =
        RouterProxy()

    @ApplicationContext
    @ApplicationScope
    @Provides
    fun context(): Context =
        application
}