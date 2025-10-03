package com.example.aqualumeapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aqualumeapp.R
import com.example.aqualumeapp.adapters.TaskAdapter
import com.example.aqualumeapp.databinding.FragmentHomeBinding
import com.example.aqualumeapp.utils.PreferencesManager
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding?= null
    private val binding get() = _binding!!
    private lateinit var prefsManager: PreferencesManager
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefsManager = PreferencesManager(requireContext())

        setupUI()
        setupClickListeners()
        loadTasks()
    }

    private fun setupUI() {
        // Set date
        val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        binding.tvDate.text = dateFormat.format(Date())

        // Setup RecyclerView
        taskAdapter = TaskAdapter(
            onTaskClick = { task ->
                prefsManager.updateTask(task.id, !task.isCompleted)
                loadTasks()
            }
        )
        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = taskAdapter
        }
    }

    private fun setupClickListeners() {
        binding.btnUpdateWater.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_stayHydrated)
        }

        binding.btnUpdateMeditation.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_addMeditation)
        }

        binding.btnUpdateJournal.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_addMood)
        }

        binding.ivNotification.setOnClickListener {
            // Handle notification settings
        }

        binding.ivSettings.setOnClickListener {
            // Handle app settings
        }
    }

    private fun loadTasks() {
        val tasks = prefsManager.getTasks()
        taskAdapter.submitList(tasks)
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}