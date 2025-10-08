package com.example.aqualumeapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aqualumeapp.R
import com.example.aqualumeapp.adapter.WaterLogAdapter
import com.example.aqualumeapp.models.WaterLog
import com.google.android.material.button.MaterialButton
import java.util.*

class HydrationHistoryFragment : Fragment() {

    private lateinit var rvWaterLogs: RecyclerView
    private lateinit var btnBack: ImageButton
    private lateinit var btnFilter: MaterialButton
    private lateinit var waterLogAdapter: WaterLogAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_hydration_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupRecyclerView()
        setupClickListeners()
        loadWaterLogs()
    }

    private fun initViews(view: View) {
        rvWaterLogs = view.findViewById(R.id.rv_water_logs)
        btnBack = view.findViewById(R.id.iv_back)
        btnFilter = view.findViewById(R.id.btn_filter)
    }

    private fun setupRecyclerView() {
        waterLogAdapter = WaterLogAdapter(
            waterLogs = emptyList(),
            onEditClick = { waterLog ->
                // Handle edit click
                showEditDialog(waterLog)
            },
            onDeleteClick = { waterLog ->
                // Handle delete click
                showDeleteConfirmation(waterLog)
            }
        )

        rvWaterLogs.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = waterLogAdapter
            setHasFixedSize(false)
        }
    }

    private fun setupClickListeners() {
        btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        btnFilter.setOnClickListener {
            showFilterDialog()
        }
    }

    private fun loadWaterLogs() {
        // Sample data - replace with actual database query
        val sampleLogs = generateSampleLogs()
        waterLogAdapter.updateLogs(sampleLogs)
    }

    private fun generateSampleLogs(): List<WaterLog> {
        val logs = mutableListOf<WaterLog>()
        val calendar = Calendar.getInstance()

        // Today's logs
        logs.add(WaterLog(1, 250, calendar.apply {
            set(Calendar.HOUR_OF_DAY, 6)
            set(Calendar.MINUTE, 30)
        }.timeInMillis, false, 250))

        // Yesterday's logs
        calendar.add(Calendar.DAY_OF_YEAR, -1)

        logs.add(WaterLog(2, 350, calendar.apply {
            set(Calendar.HOUR_OF_DAY, 22)
            set(Calendar.MINUTE, 0)
        }.timeInMillis, true, 3000))

        logs.add(WaterLog(3, 400, calendar.apply {
            set(Calendar.HOUR_OF_DAY, 20)
            set(Calendar.MINUTE, 45)
        }.timeInMillis, false, 2650))

        logs.add(WaterLog(4, 350, calendar.apply {
            set(Calendar.HOUR_OF_DAY, 19)
            set(Calendar.MINUTE, 25)
        }.timeInMillis, false, 2250))

        logs.add(WaterLog(5, 500, calendar.apply {
            set(Calendar.HOUR_OF_DAY, 17)
            set(Calendar.MINUTE, 40)
        }.timeInMillis, false, 1900))

        logs.add(WaterLog(6, 400, calendar.apply {
            set(Calendar.HOUR_OF_DAY, 15)
            set(Calendar.MINUTE, 30)
        }.timeInMillis, false, 1400))

        logs.add(WaterLog(7, 250, calendar.apply {
            set(Calendar.HOUR_OF_DAY, 13)
            set(Calendar.MINUTE, 10)
        }.timeInMillis, false, 1000))

        logs.add(WaterLog(8, 300, calendar.apply {
            set(Calendar.HOUR_OF_DAY, 11)
            set(Calendar.MINUTE, 40)
        }.timeInMillis, false, 750))

        logs.add(WaterLog(9, 350, calendar.apply {
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 10)
        }.timeInMillis, false, 450))

        logs.add(WaterLog(10, 250, calendar.apply {
            set(Calendar.HOUR_OF_DAY, 6)
            set(Calendar.MINUTE, 40)
        }.timeInMillis, false, 250))

        return logs
    }

    private fun showEditDialog(waterLog: WaterLog) {
        // Implement edit dialog
        // You can use a DialogFragment or BottomSheet here
    }

    private fun showDeleteConfirmation(waterLog: WaterLog) {
        // Implement delete confirmation dialog
    }

    private fun showFilterDialog() {
        // Implement filter dialog (date picker, custom range, etc.)
    }
}