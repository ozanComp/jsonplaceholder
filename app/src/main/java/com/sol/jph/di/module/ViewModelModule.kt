package com.sol.jph.di.module

import androidx.lifecycle.ViewModel
import com.sol.jph.di.factory.ViewModelKey
import com.sol.jph.ui.viewmodel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    internal abstract fun bindSignInViewModel(viewModel: SignInViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PostViewModel::class)
    internal abstract fun bindPostViewModel(viewModel: PostViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    internal abstract fun bindUserViewModel(viewModel: UserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CommentViewModel::class)
    internal abstract fun bindCommentViewModel(viewModel: CommentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PhotoViewModel::class)
    internal abstract fun bindPhotoViewModel(viewModel: PhotoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    internal abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    internal abstract fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel
}