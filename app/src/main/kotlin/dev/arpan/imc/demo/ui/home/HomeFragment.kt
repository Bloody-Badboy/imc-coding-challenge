package dev.arpan.imc.demo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dev.arpan.imc.demo.R
import dev.arpan.imc.demo.databinding.FragmentHomeBinding
import dev.arpan.imc.demo.ui.NavigationDestinationFragment
import dev.arpan.imc.demo.ui.home.HomeFragmentDirections.Companion.toNavProfile
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : NavigationDestinationFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = checkNotNull(_binding)
    private val viewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener when (it.itemId) {
                R.id.home_menu_profile -> {
                    findNavController().navigate(toNavProfile())
                    true
                }
                else -> false
            }
        }
    }
}
