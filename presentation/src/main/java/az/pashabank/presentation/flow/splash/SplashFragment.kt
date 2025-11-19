package az.pashabank.presentation.flow.splash

import android.view.LayoutInflater
import android.view.ViewGroup
import az.pashabank.starter.presentation.databinding.FragmentSplashBinding
import az.pashabank.presentation.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : BaseFragment<SplashState, Nothing, SplashViewModel, FragmentSplashBinding>() {

    override val viewModel by viewModel<SplashViewModel>()
    override val bindingCallback: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSplashBinding
        get() = FragmentSplashBinding::inflate
    override val screenName: String
        get() = "splash"

    override fun observeState(state: SplashState) {
        when (state) {
            SplashState.ProceedWithAuthorization -> proceedWithAuthorization()
            SplashState.ProceedWithOnBoarding -> proceedWithOnBoarding()
        }
    }

    private fun proceedWithOnBoarding() {
        viewModel.navigate(SplashFragmentDirections.toLogin())
    }

    private fun proceedWithAuthorization() {
        navigateToMain()
    }

    private fun navigateToMain() {
        toMain()
    }
}