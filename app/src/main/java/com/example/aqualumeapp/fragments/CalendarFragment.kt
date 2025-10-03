package com.example.aqualumeapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.aqualumeapp.R
import com.example.aqualumeapp.databinding.FragmentCalendarBinding
import com.example.aqualumeapp.utils.PreferencesManager
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefsManager: PreferencesManager
    private var selectedDate: Long = System.currentTimeMillis()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefsManager = PreferencesManager(requireContext())

        setupCalendar()
        setupClickListeners()
        loadDataForSelectedDate()
    }

    private fun setupCalendar() {
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDate = calendar.timeInMillis
            loadDataForSelectedDate()
        }
    }

    private fun setupClickListeners() {
        binding.btnUpdateWater.setOnClickListener {
            findNavController().navigate(R.id.action_calendar_to_stayHydrated)
        }

        binding.btnUpdateMeditation.setOnClickListener {
            findNavController().navigate(R.id.action_calendar_to_addMeditation)
        }

        binding.btnUpdateJournal.setOnClickListener {
            findNavController().navigate(R.id.action_calendar_to_addMood)
        }
    }

    private fun loadDataForSelectedDate() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = selectedDate

        val startOfDay = calendar.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }.timeInMillis

        val endOfDay = calendar.apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }.timeInMillis

        // Load water data
        val waterLogs = prefsManager.getWaterLogs()
            .filter { it.timestamp in startOfDay..endOfDay }
        val totalWater = waterLogs.sumOf { it.amount }
        val waterGoal = prefsManager.getWaterGoal()
        val waterPercentage = if (waterGoal > 0) (totalWater.toFloat() / waterGoal * 100).toInt() else 0

        binding.tvWaterPercentage.text = "$waterPercentage%"
        binding.tvWaterCompleted.text = "Completed"
        binding.tvWaterTarget.text = "Target - ${waterGoal / 1000} liters"
        binding.progressWater.progress = waterPercentage

        // Load meditation data
        val meditationLogs = prefsManager.getMeditationLogs()
            .filter { it.timestamp in startOfDay..endOfDay }
        val totalMeditation = meditationLogs.sumOf { it.duration }
        val meditationGoal = prefsManager.getMeditationGoal()
        val meditationPercentage = if (meditationGoal > 0) (totalMeditation.toFloat() / meditationGoal * 100).toInt() else 0

        binding.tvMeditationPercentage.text = "$meditationPercentage%"
        binding.tvMeditationCompleted.text = "Completed"
        binding.tvMeditationTarget.text = "Target - $meditationGoal minutes"
        binding.progressMeditation.progress = meditationPercentage

        // Load mood data
        val moodLogs = prefsManager.getMoodLogs()
            .filter { it.timestamp in startOfDay..endOfDay }

        val moodSummary = if (moodLogs.isNotEmpty()) {
            val dominantMood = moodLogs.groupBy { it.mood }
                .maxByOrNull { it.value.size }?.key ?: "neutral"
            "You have been mostly feeling $dominantMood"
        } else {
            "No mood entries for this day"
        }

        binding.tvMoodSummary.text = moodSummary
        val moodPercentage = if (moodLogs.isNotEmpty()) 100 else 0
        binding.progressMood.progress = moodPercentage
    }

    override fun onResume() {
        super.onResume()
        loadDataForSelectedDate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}