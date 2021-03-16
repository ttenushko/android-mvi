package com.ttenushko.androidmvi.demo.di.presentation.screen

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.ttenushko.androidmvi.demo.R
import com.ttenushko.androidmvi.demo.di.PublicDependencies
import com.ttenushko.androidmvi.demo.di.domain.UseCaseModule
import com.ttenushko.androidmvi.demo.di.presentation.screen.addplace.AddPlaceFragmentDependencies
import com.ttenushko.androidmvi.demo.di.presentation.screen.addplace.AddPlaceFragmentModule
import com.ttenushko.androidmvi.demo.di.presentation.screen.addplace.DaggerAddPlaceFragmentComponent
import com.ttenushko.androidmvi.demo.di.presentation.screen.places.DaggerPlacesFragmentComponent
import com.ttenushko.androidmvi.demo.di.presentation.screen.places.PlacesFragmentDependencies
import com.ttenushko.androidmvi.demo.di.presentation.screen.places.PlacesFragmentModule
import com.ttenushko.androidmvi.demo.presentation.screens.AppRouterImpl
import com.ttenushko.androidmvi.demo.presentation.screens.MainActivity
import com.ttenushko.androidmvi.demo.presentation.screens.addplace.AddPlaceFragment
import com.ttenushko.androidmvi.demo.presentation.screens.places.PlacesFragment
import com.ttenushko.androidmvi.demo.presentation.utils.FragmentFactory
import com.ttenushko.androidmvi.demo.presentation.utils.ViewModelFactory
import com.ttenushko.androidmvi.demo.presentation.utils.dagger.ActivityScope
import com.ttenushko.androidmvi.demo.presentation.utils.dagger.FragmentKey
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider

@Component(
    dependencies = [
        PublicDependencies::class
    ],
    modules = [
        MainActivityModule::class,
        DependenciesProviderModule::class,
        UseCaseModule::class
    ]
)
@ActivityScope
interface MainActivityComponent : PlacesFragmentDependencies, AddPlaceFragmentDependencies {

    @Component.Builder
    interface Builder {
        fun publicDependencies(publicComponents: PublicDependencies): Builder
        fun activityModule(module: MainActivityModule): Builder
        fun useCaseModule(module: UseCaseModule): Builder
        fun build(): MainActivityComponent
    }

    fun inject(activity: MainActivity)
}

@Module(
    includes = [
        PlacesFragmentProviderModule::class,
        AddPlaceFragmentProviderModule::class
    ]
)
class MainActivityModule(
    private val activity: AppCompatActivity
) {

    @ActivityScope
    @Provides
    fun viewModelFactory(providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelProvider.Factory =
        ViewModelFactory(providers)

    @ActivityScope
    @Provides
    fun fragmentFactory(providers: Map<Class<out Fragment>, @JvmSuppressWildcards Provider<Fragment>>): androidx.fragment.app.FragmentFactory =
        FragmentFactory(providers)

    @Provides
    fun routerImpl(): AppRouterImpl =
        AppRouterImpl(activity.supportFragmentManager.findFragmentById(R.id.nav_host_main) as NavHostFragment)

//    @ActivityScope
//    @Provides
//    fun taskExecutorFactory(): TaskExecutorFactory =
//        TaskExecutorFactory.create(activity.lifecycleScope)
}

@Module
interface DependenciesProviderModule {
    @Binds
    fun placesFragmentDependencies(component: MainActivityComponent): PlacesFragmentDependencies

    @Binds
    fun addPlaceFragmentDependencies(component: MainActivityComponent): AddPlaceFragmentDependencies
}

@Module(includes = [PlacesFragmentProviderModule.Bindings::class])
class PlacesFragmentProviderModule {

    @Provides
    fun fragment(dependencies: PlacesFragmentDependencies): PlacesFragment =
        DaggerPlacesFragmentComponent.builder()
            .dependencies(dependencies = dependencies)
            .fragmentModule(PlacesFragmentModule())
            .build()
            .fragment()

    @Module
    interface Bindings {
        @Binds
        @IntoMap
        @FragmentKey(PlacesFragment::class)
        fun bindFragment(fragment: PlacesFragment): Fragment
    }
}

@Module(includes = [AddPlaceFragmentProviderModule.Bindings::class])
class AddPlaceFragmentProviderModule {

    @Provides
    fun fragment(dependencies: AddPlaceFragmentDependencies): AddPlaceFragment =
        DaggerAddPlaceFragmentComponent.builder()
            .dependencies(dependencies = dependencies)
            .fragmentModule(AddPlaceFragmentModule())
            .build()
            .fragment()

    @Module
    interface Bindings {
        @Binds
        @IntoMap
        @FragmentKey(AddPlaceFragment::class)
        fun bindFragment(fragment: AddPlaceFragment): Fragment
    }
}
