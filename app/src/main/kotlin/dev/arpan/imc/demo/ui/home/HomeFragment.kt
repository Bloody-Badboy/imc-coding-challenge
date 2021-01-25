package dev.arpan.imc.demo.ui.home

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dev.arpan.imc.demo.R
import dev.arpan.imc.demo.background.NotificationSchedulerWorker
import dev.arpan.imc.demo.databinding.FragmentHomeBinding
import dev.arpan.imc.demo.ui.NavigationDestinationFragment
import dev.arpan.imc.demo.ui.home.HomeFragmentDirections.Companion.toNavProfile
import dev.arpan.imc.demo.utils.DateUtils.getFormattedDate
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.Calendar
import java.util.TimeZone

class HomeFragment : NavigationDestinationFragment() {

    companion object {
        private const val DATE_PICKER_TAG = "date_picker"
        private const val TIME_PICKER_TAG = "time_picker"
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = checkNotNull(_binding)
    private val viewModel: HomeViewModel by viewModel()

    private val pickerCalender: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.btnHomeSchedule.setOnClickListener {
            showDatePicker()
        }
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

    private fun showDatePicker() {
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendarConstraints = CalendarConstraints.Builder()
            .setStart(today)
            .setValidator(DateValidatorPointForward.now())
            .build()
        val picker = MaterialDatePicker.Builder.datePicker()
            .setSelection(today)
            .setCalendarConstraints(calendarConstraints)
            .setTitleText(getString(R.string.home_date_picker_title))
            .build()
        picker.show(childFragmentManager, DATE_PICKER_TAG)
        picker.addOnPositiveButtonClickListener {
            pickerCalender.clear()
            pickerCalender.timeInMillis = picker.selection ?: 0
            showTimePicker()
        }
    }

    private fun showTimePicker() {
        val pickerCalendar = Calendar.getInstance(TimeZone.getDefault())
        val isSystem24Hour = DateFormat.is24HourFormat(requireContext())
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
        val hour = pickerCalendar.get(Calendar.HOUR_OF_DAY)
        val minute = pickerCalendar.get(Calendar.MINUTE)
        val picker = MaterialTimePicker.Builder()
            .setTitleText(getString(R.string.home_time_picker_title))
            .setTimeFormat(clockFormat)
            .setHour(hour)
            .setMinute(minute)
            .build()
        picker.show(childFragmentManager, TIME_PICKER_TAG)
        picker.addOnPositiveButtonClickListener {
            pickerCalendar.set(Calendar.HOUR_OF_DAY, picker.hour)
            pickerCalendar.set(Calendar.MINUTE, picker.minute)
            pickerCalendar.set(Calendar.SECOND, 0)
            pickerCalendar.set(Calendar.MILLISECOND, 0)
            scheduleNotification(pickerCalendar.timeInMillis)
        }
    }

    private fun scheduleNotification(selectedTimeInMillis: Long) {
        val calenderNow = Calendar.getInstance().apply {
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val nowMillis = calenderNow.timeInMillis
        val delay = if (pickerCalender[Calendar.MINUTE] == calenderNow[Calendar.MINUTE])
            0
        else
            selectedTimeInMillis - nowMillis

        if (delay < 0) {
            showSnack(getString(R.string.home_picker_time_error, getFormattedDate(nowMillis)))
        } else {
            showSnack(
                getString(
                    R.string.home_picker_selected_time,
                    getFormattedDate(selectedTimeInMillis)
                )
            )
            viewModel.setNotifyTriggerMillis(selectedTimeInMillis)
            NotificationSchedulerWorker.start(requireContext(), delay)
        }
    }

    private fun showSnack(msg: String) {
        Snackbar.make(
            requireNotNull(view as CoordinatorLayout?) {
                "Fragment root view must be a CoordinatorLayout"
            },
            msg, Snackbar.LENGTH_LONG
        ).show()
    }
}
