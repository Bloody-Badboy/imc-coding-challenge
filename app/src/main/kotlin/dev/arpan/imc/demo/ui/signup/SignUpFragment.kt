package dev.arpan.imc.demo.ui.signup

import android.Manifest
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteConstraintException
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.arpan.imc.demo.R
import dev.arpan.imc.demo.data.model.CurrentLocation
import dev.arpan.imc.demo.databinding.FragmentSignupBinding
import dev.arpan.imc.demo.location.LocationRetriever
import dev.arpan.imc.demo.location.LocationRetriever.Result
import dev.arpan.imc.demo.ui.NavigationDestinationFragment
import dev.arpan.imc.demo.utils.EventObserver
import dev.arpan.imc.demo.utils.hasLocationPermission
import dev.arpan.imc.demo.utils.toToast
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class SignUpFragment : NavigationDestinationFragment() {

    companion object {
        const val REQUEST_CHECK_SETTINGS = 101
        const val PERMISSIONS_REQUEST_LOCATION = 101
        const val EXTRA_REQUEST_RATIONALE_SHOWN_KEY = "request_rationale_shown"
    }

    private var _binding: FragmentSignupBinding? = null
    private val binding: FragmentSignupBinding
        get() = checkNotNull(_binding)
    private val viewModel: SignUpViewModel by viewModel()
    private lateinit var retriever: LocationRetriever
    private var isRequestRationaleShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        checkLocationPermission()

        viewModel.signUpResult.observe(
            viewLifecycleOwner,
            EventObserver {
                handleSignUpResult(it)
            }
        )

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        if (::retriever.isInitialized) {
            retriever.stop()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(EXTRA_REQUEST_RATIONALE_SHOWN_KEY, isRequestRationaleShown)
        super.onSaveInstanceState(outState)
    }

    private fun onRestoreInstanceState(savedInstanceState: Bundle) {
        with(savedInstanceState) {
            if (containsKey(EXTRA_REQUEST_RATIONALE_SHOWN_KEY)) {
                isRequestRationaleShown = getBoolean(EXTRA_REQUEST_RATIONALE_SHOWN_KEY)
            }
        }
    }

    private fun handleSignUpResult(result: SignUpViewModel.SignUpResult) {
        when (result) {
            is SignUpViewModel.SignUpResult.Error -> {
                if (result.throwable is SQLiteConstraintException) {
                    getString(R.string.sign_up_email_already_exists).toToast(requireContext())
                } else {
                    getString(R.string.all_unexpected_error).toToast(requireContext())
                }
            }
            is SignUpViewModel.SignUpResult.Success -> {
                getString(R.string.sign_up_success).toToast(requireContext())
                findNavController().navigateUp()
            }
        }
    }

    private fun checkLocationPermission() {
        if (requireContext().hasLocationPermission) {
            Timber.d("Location permission granted.")
            startLocationRetriever()
        } else {
            Timber.d("Location permission denied, requesting location permission.")
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_LOCATION
            )
        }
    }

    private fun showLocationPermissionDeniedDialog() {
        MaterialAlertDialogBuilder(requireActivity()).apply {
            setCancelable(false)
            setTitle(R.string.all_permission_denied)
            setMessage(
                getString(R.string.all_location_permission_required_msg)
            )
            setPositiveButton(
                getString(R.string.all_btn_settings)
            ) { _, _ ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:${requireActivity().packageName}")
                )
                findNavController().navigateUp()
                startActivity(intent)
            }
            setNegativeButton(android.R.string.cancel) { _, _ ->
                findNavController().navigateUp()
            }
        }.show()
    }

    private fun showLocationNotEnabledDialog() {
        MaterialAlertDialogBuilder(requireActivity()).apply {
            setCancelable(false)
            setTitle(R.string.all_enable_location_provider)
            setMessage(R.string.all_device_location_not_enabled)
            setPositiveButton(R.string.all_btn_enable) { _, _ ->
                startLocationRetriever()
            }
            setNegativeButton(android.R.string.cancel) { _, _ ->
                findNavController().navigateUp()
            }
        }.show()
    }

    private fun startLocationRetriever() {
        if (!::retriever.isInitialized) {
            retriever = LocationRetriever(
                context = requireContext(),
                onLocationResult = { result ->
                    when (result) {
                        is Result.RequestingLocationUpdate -> {
                            viewModel.setRequestingLocationUpdate(true)
                        }
                        is Result.LocationUpdate -> {
                            retriever.stop()
                            viewModel.setCurrentLocation(
                                CurrentLocation(
                                    longitude = result.longitude,
                                    latitude = result.latitude
                                )
                            )
                        }
                        is Result.StoppedLocationUpdate -> {
                            viewModel.setRequestingLocationUpdate(false)
                        }
                    }
                },
                onResultResolutionRequired = {
                    startIntentSenderForResult(
                        it.resolution.intentSender,
                        REQUEST_CHECK_SETTINGS,
                        null,
                        0,
                        0,
                        0,
                        null
                    )
                },
                onResultSettingsChangeUnavailable = {
                }
            )
        }
        retriever.start()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Timber.d("Location Permission Granted.")
                    startLocationRetriever()
                } else {
                    Timber.d("Location Permission Denied.")
                    if (isRequestRationaleShown) {
                        showLocationPermissionDeniedDialog()
                    } else {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                            isRequestRationaleShown = true
                            Toast.makeText(
                                requireContext(),
                                "Location permission required for sign up.",
                                Toast.LENGTH_LONG
                            ).show()
                            checkLocationPermission()
                        } else {
                            showLocationPermissionDeniedDialog()
                        }
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                RESULT_OK -> {
                    Timber.d("All required changes were successfully made")
                    startLocationRetriever()
                }
                RESULT_CANCELED -> showLocationNotEnabledDialog()
            }
        }
    }
}
