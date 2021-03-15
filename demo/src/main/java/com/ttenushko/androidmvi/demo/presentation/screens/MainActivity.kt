package com.ttenushko.androidmvi.demo.presentation.screens

import android.os.Bundle
import androidx.fragment.app.FragmentFactory
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ttenushko.androidmvi.demo.R
import com.ttenushko.androidmvi.demo.databinding.ActivityMainBinding
import com.ttenushko.androidmvi.demo.di.domain.UseCaseModule
import com.ttenushko.androidmvi.demo.di.presentation.screen.DaggerMainActivityComponent
import com.ttenushko.androidmvi.demo.di.presentation.screen.MainActivityModule
import com.ttenushko.androidmvi.demo.presentation.base.BaseActivity
import com.ttenushko.androidmvi.demo.presentation.base.Router
import com.ttenushko.androidmvi.demo.presentation.base.asProxy
import com.ttenushko.androidmvi.demo.presentation.utils.dagger.findDependency
import dagger.Lazy
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    @Inject
    lateinit var routerImpl: Lazy<AppRouterImpl>

    @Inject
    lateinit var router: Router<AppRouter.Destination>

    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {

        DaggerMainActivityComponent.builder()
            .publicDependencies(findDependency())
            .activityModule(MainActivityModule(this))
            .useCaseModule(UseCaseModule())
            .build()
            .inject(this)

        supportFragmentManager.fragmentFactory = fragmentFactory

        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
            .also { setContentView(it.root) }
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_main) as NavHostFragment
        navController = navHostFragment.navController

        navHostFragment.childFragmentManager.addOnBackStackChangedListener { handleFragmentBackStackChanged() }
    }

    override fun onStart() {
        super.onStart()
        handleFragmentBackStackChanged()
        router.asProxy()?.attach(routerImpl.get())
    }

    override fun onStop() {
        super.onStop()
        router.asProxy()?.detach(routerImpl.get())
    }

    override fun onBackPressed() {
        if (!navController.popBackStack()) {
            finish()
        }
    }

    private fun handleFragmentBackStackChanged() {
        // do nothing
    }
}