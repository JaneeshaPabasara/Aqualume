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
import com.example.aqualumeapp.databinding.FragmentAddBinding
import com.example.aqualumeapp.models.Task
import com.example.aqualumeapp.utils.PreferencesManager

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefsManager: PreferencesManager
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefsManager = PreferencesManager(requireContext())

        setupClickListeners()
        setupTaskRecyclerView()
        loadTasks()
    }

    private fun setupClickListeners() {
        binding.cardWater.setOnClickListener {
            findNavController().navigate(R.id.action_add_to_water)
        }

        binding.cardMeditation.setOnClickListener {
            findNavController().navigate(R.id.action_add_to_meditate)
        }

        binding.cardJournal.setOnClickListener {
            findNavController().navigate(R.id.action_add_to_journal)
        }

        binding.cardMedicine.setOnClickListener {
            // Navigate to medicine screen if needed
        }
    }

    private fun setupTaskRecyclerView() {
        taskAdapter = TaskAdapter { task ->
            // Toggle task completion
            prefsManager.updateTaskCompletion(task.id, !task.isCompleted)
            loadTasks()
        }

        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = taskAdapter
        }
    }

    private fun loadTasks() {
        // Get all tasks
        val allTasks = prefsManager.getAllTasks().toMutableList()

        // Generate tasks from user settings
        val settings = prefsManager.getUserSettings()

        // Add water task if not exists
        if (allTasks.none { it.title.contains("water", ignoreCase = true) }) {
            val waterTask = Task(
                title = "Drink water - ${settings.waterGoal / 1000} liters",
                isCompleted = false
            )
            prefsManager.saveTask(waterTask)
            allTasks.add(waterTask)
        }

        // Add meditation task if not exists
        if (allTasks.none { it.title.contains("meditate", ignoreCase = true) }) {
            val meditationTask = Task(
                title = "Meditate ${settings.meditationGoal} min",
                isCompleted = false
            )
            prefsManager.saveTask(meditationTask)
            allTasks.add(meditationTask)
        }

        // Add journal task if not exists
        if (allTasks.none { it.title.contains("journal", ignoreCase = true) }) {
            val journalTask = Task(
                title = "Write the journal",
                isCompleted = false
            )
            prefsManager.saveTask(journalTask)
            allTasks.add(journalTask)
        }

        taskAdapter.submitList(allTasks.sortedBy { it.isCompleted })
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