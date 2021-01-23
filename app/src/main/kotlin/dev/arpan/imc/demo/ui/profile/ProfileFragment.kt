package dev.arpan.imc.demo.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.arpan.imc.demo.databinding.FragmentProfileBinding
import dev.arpan.imc.demo.prefs.PreferenceStorage
import dev.arpan.imc.demo.ui.NavigationDestinationFragment
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileFragment : NavigationDestinationFragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = checkNotNull(_binding)
    private val viewModel: ProfileViewModel by viewModel()
    private val preferenceStorage: PreferenceStorage by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.btnProfileLogout.setOnClickListener {
            preferenceStorage.loggedInUserEmail = null
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
