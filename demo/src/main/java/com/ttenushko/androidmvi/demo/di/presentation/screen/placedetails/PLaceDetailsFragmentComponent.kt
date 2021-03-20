package com.ttenushko.androidmvi.demo.di.presentation.screen.placedetails

import com.squareup.picasso.Picasso
import com.ttenushko.androidmvi.demo.domain.application.usecase.DeletePlaceUseCase
import com.ttenushko.androidmvi.demo.domain.weather.usecase.GetCurrentWeatherConditionsUseCase
import com.ttenushko.androidmvi.demo.presentation.base.Router
import com.ttenushko.androidmvi.demo.presentation.screens.AppRouter
import com.ttenushko.androidmvi.demo.presentation.screens.placedetails.PlaceDetailsFragment
import com.ttenushko.androidmvi.demo.presentation.screens.placedetails.PlaceDetailsViewModel
import com.ttenushko.androidmvi.demo.presentation.utils.MviEventLogger
import com.ttenushko.androidmvi.demo.presentation.utils.MviLogger
import dagger.Component
import dagger.Provides

interface PlaceDetailsFragmentDependencies {
    fun getCurrentWeatherConditionsUseCase(): GetCurrentWeatherConditionsUseCase
    fun deletePlaceUseCase(): DeletePlaceUseCase
    fun picasso(): Picasso
    fun router(): Router<AppRouter.Destination>
}

@Component(
    dependencies = [PlaceDetailsFragmentDependencies::class],
    modules = [PlaceDetailsFragmentModule::class]
)
internal interface PlaceDetailsFragmentComponent {

    @Component.Builder
    interface Builder {
        fun dependencies(dependencies: PlaceDetailsFragmentDependencies): Builder
        fun fragmentModule(module: PlaceDetailsFragmentModule): Builder
        fun build(): PlaceDetailsFragmentComponent
    }

    fun fragment(): PlaceDetailsFragment
}

@dagger.Module
internal class PlaceDetailsFragmentModule() {
    @Suppress("UNCHECKED_CAST")
    @Provides
    fun placeDetailsFragment(
        getCurrentWeatherConditionsUseCase: GetCurrentWeatherConditionsUseCase,
        deletePlaceUseCase: DeletePlaceUseCase,
        picasso: Picasso,
        router: Router<AppRouter.Destination>
    ): PlaceDetailsFragment =
        PlaceDetailsFragment(
            router = router,
            eventLogger = MviEventLogger("PlaceDetailsFragment"),
            picasso = picasso,
            viewModelProvider = { placeId, savedState ->
                PlaceDetailsViewModel(
                    mviLogger = MviLogger("PlaceDetailsFragment"),
                    placeId = placeId,
                    getCurrentWeatherConditionsUseCase = getCurrentWeatherConditionsUseCase,
                    deletePlaceUseCase = deletePlaceUseCase,
                    savedState = savedState
                )
            }
        )
}



