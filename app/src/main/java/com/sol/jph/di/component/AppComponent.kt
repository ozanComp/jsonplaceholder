package com.sol.jph.di.component

import com.sol.jph.di.module.ViewModelModule
import com.sol.jph.di.module.NetworkModule
import com.sol.jph.di.module.RoomModule
import com.sol.jph.di.module.ServiceModule
import com.sol.jph.ui.service.ToastService
import com.sol.jph.ui.view.activity.DetailActivity
import com.sol.jph.ui.view.activity.MainActivity
import com.sol.jph.ui.view.activity.SplashActivity
import com.sol.jph.ui.view.fragment.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RoomModule::class, ViewModelModule::class, NetworkModule::class, ServiceModule::class])
interface AppComponent {
    fun inject(loginFragment: LoginFragment)
    fun inject(signInFragment: SignInFragment)
    fun inject(postFragment: PostFragment)
    fun inject(userFragment: UserFragment)
    fun inject(commentFragment: CommentFragment)
    fun inject(photoFragment: PhotoFragment)
    fun inject(homeFragment: HomeFragment)
    fun inject(mainActivity: MainActivity)
    fun inject(detailActivity: DetailActivity)
    fun inject(splashActivity: SplashActivity)
    fun inject(toastService: ToastService)
}