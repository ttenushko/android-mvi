package com.ttenushko.androidmvi.demo.di.presentation.screen.addplace

import com.ttenushko.androidmvi.demo.domain.application.usecase.SavePlaceUseCase
import com.ttenushko.androidmvi.demo.domain.weather.usecase.SearchPlaceUseCase
import com.ttenushko.androidmvi.demo.presentation.base.Router
import com.ttenushko.androidmvi.demo.presentation.screens.AppRouter
import com.ttenushko.androidmvi.demo.presentation.screens.addplace.AddPlaceFragment
import com.ttenushko.androidmvi.demo.presentation.screens.addplace.AddPlaceViewModel
import com.ttenushko.androidmvi.demo.presentation.utils.MviEventLogger
import com.ttenushko.androidmvi.demo.presentation.utils.MviLogger
import com.ttenushko.androidmvi.demo.presentation.utils.dagger.FragmentScope
import dagger.Component
import dagger.Module
import dagger.Provides

interface AddPlaceFragmentDependencies {
    fun searchPlaceUseCase(): SearchPlaceUseCase
    fun savePlaceUseCase(): SavePlaceUseCase
    fun router(): Router<AppRouter.Destination>
}

@FragmentScope
@Component(
    dependencies = [AddPlaceFragmentDependencies::class],
    modules = [AddPlaceFragmentModule::class]
)
interface AddPlaceFragmentComponent {

    @Component.Builder
    interface Builder {
        fun dependencies(dependencies: AddPlaceFragmentDependencies): Builder
        fun fragmentModule(module: AddPlaceFragmentModule): Builder
        fun build(): AddPlaceFragmentComponent
    }

    fun fragment(): AddPlaceFragment
}

@Module
class AddPlaceFragmentModule {

    @JvmSuppressWildcards
    @Provides
    fun placesFragment(
        searchPlaceUseCase: SearchPlaceUseCase,
        savePlaceUseCase: SavePlaceUseCase,
        router: Router<AppRouter.Destination>
    ): AddPlaceFragment =
        AddPlaceFragment(
            router = router,
            eventLogger = MviEventLogger("AddPlaceFragment"),
            viewModelProvider = { search, savedState ->
                AddPlaceViewModel(
                    mviLogger = MviLogger("AddPlaceFragment"),
                    search = search,
                    searchPlaceUseCase = searchPlaceUseCase,
                    savePlaceUseCase = savePlaceUseCase,
                    savedState = savedState
                )
            }
        )
}