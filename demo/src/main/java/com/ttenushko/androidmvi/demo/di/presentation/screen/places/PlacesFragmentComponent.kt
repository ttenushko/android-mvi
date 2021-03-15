package com.ttenushko.androidmvi.demo.di.presentation.screen.places

import com.ttenushko.androidmvi.demo.domain.application.usecase.TrackSavedPlacesUseCase
import com.ttenushko.androidmvi.demo.presentation.base.Router
import com.ttenushko.androidmvi.demo.presentation.screens.AppRouter
import com.ttenushko.androidmvi.demo.presentation.screens.places.PlacesFragment
import com.ttenushko.androidmvi.demo.presentation.screens.places.PlacesFragmentViewModel
import com.ttenushko.androidmvi.demo.presentation.utils.MviEventLogger
import com.ttenushko.androidmvi.demo.presentation.utils.MviLogger
import com.ttenushko.androidmvi.demo.presentation.utils.dagger.FragmentScope
import dagger.Component
import dagger.Module
import dagger.Provides

interface PlacesFragmentDependencies {
    fun trackSavedPlacesUseCase(): TrackSavedPlacesUseCase
    fun router(): Router<AppRouter.Destination>
}

@FragmentScope
@Component(
    dependencies = [PlacesFragmentDependencies::class],
    modules = [PlacesFragmentModule::class]
)
interface PlacesFragmentComponent {

    @Component.Builder
    interface Builder {
        fun dependencies(dependencies: PlacesFragmentDependencies): Builder
        fun fragmentModule(module: PlacesFragmentModule): Builder
        fun build(): PlacesFragmentComponent
    }

    fun fragment(): PlacesFragment
}

@Module
class PlacesFragmentModule {

    @JvmSuppressWildcards
    @Provides
    fun placesFragment(
        trackSavedPlacesUseCase: TrackSavedPlacesUseCase,
        router: Router<AppRouter.Destination>
    ): PlacesFragment =
        PlacesFragment(
            router = router,
            eventLogger = MviEventLogger("PlacesFragment"),
            viewModelProvider = { savedState ->
                PlacesFragmentViewModel(
                    mviLogger = MviLogger("PlacesFragment"),
                    trackSavedPlacesUseCase = trackSavedPlacesUseCase,
                    savedState = savedState
                )
            }
        )
}