package com.example.aqualumeapp.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aqualumeapp.R
import com.example.aqualumeapp.adapters.WaterLogAdapter
import com.example.aqualumeapp.databinding.FragmentHydrationHistoryBinding
import com.example.aqualumeapp.models.WaterLog
import com.example.aqualumeapp.utils.PreferencesManager
import java.text.SimpleDateFormat
import java.util.*

class HydrationHistoryFragment : Fragment() {

    private var _binding: FragmentHydrationHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefsManager: PreferencesManager
    private lateinit var waterLogAdapter: WaterLogAdapter
    private var selectedDate: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHydrationHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefsManager = PreferencesManager(requireContext())

        setupRecyclerView()
        setupClickListeners()
        loadWaterLogs()
    }

    private fun setupRecyclerView() {
        waterLogAdapter = WaterLogAdapter(
            onEditClick = { log ->
                val bundle = bundleOf("logId" to log.id)
                findNavController().navigate(R.id.action_hydrationHistory_to_stayHydrated, bundle)
            },
            onDeleteClick = { log ->
                prefsManager.deleteWaterLog(log.id)
                loadWaterLogs()
            }
        )

        binding.rvWaterLogs.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = waterLogAdapter
        }
    }

    private fun setupClickListeners() {
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnFilter.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay, 0, 0, 0)
                selectedDate = selectedCalendar.timeInMillis
                loadWaterLogs()
            },
            year,
            month,
            day
        ).show()
    }

    private fun loadWaterLogs() {
        val allLogs = prefsManager.getWaterLogs()

        val filteredLogs = if (selectedDate != null) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = selectedDate!!
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

            allLogs.filter { it.timestamp in startOfDay..endOfDay }
        } else {
            allLogs
        }

        // Group by date
        val groupedLogs = filteredLogs.groupBy {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it.timestamp
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)
        }

        // Display grouped logs
        waterLogAdapter.submitList(filteredLogs.sortedByDescending { it.timestamp })

        // Check if any logs completed goal
        checkGoalCompletion(groupedLogs)
    }

    private fun checkGoalCompletion(groupedLogs: Map<String, List<WaterLog>>) {
        val goal = prefsManager.getWaterGoal()
        groupedLogs.forEach { (_, logs) ->
            val total = logs.sumOf { it.amount }
            // You can add visual indicators here if goal is met
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}