package dev.arpan.imc.demo.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dev.arpan.imc.demo.R
import dev.arpan.imc.demo.databinding.FragmentLoginBinding
import dev.arpan.imc.demo.ui.NavigationDestinationFragment
import dev.arpan.imc.demo.ui.login.LoginFragmentDirections.Companion.toNavHome
import dev.arpan.imc.demo.ui.login.LoginFragmentDirections.Companion.toNavSignup
import dev.arpan.imc.demo.utils.EventObserver
import dev.arpan.imc.demo.utils.toToast
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : NavigationDestinationFragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = checkNotNull(_binding)
    private val viewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.navigateToSignUp.observe(
            viewLifecycleOwner,
            EventObserver {
                findNavController().navigate(toNavSignup())
            }
        )

        viewModel.loginResult.observe(
            viewLifecycleOwner,
            EventObserver {
                handleLoginResult(it)
            }
        )

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun handleLoginResult(result: LoginViewModel.LoginResult) {
        when (result) {
            is LoginViewModel.LoginResult.Success -> {
                findNavController().navigate(toNavHome())
            }
            is LoginViewModel.LoginResult.Error -> {
                getString(R.string.login_invalid_credentials).toToast(requireContext())
            }
        }
    }
}
