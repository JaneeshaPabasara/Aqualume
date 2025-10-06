package com.example.aqualumeapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.aqualumeapp.databinding.FragmentAddMoodBinding
import com.example.aqualumeapp.models.MoodLog
import com.example.aqualumeapp.utils.PreferencesManager
import java.text.SimpleDateFormat
import java.util.*
import com.example.aqualumeapp.adapters.MoodLogAdapter


class AddMoodFragment : Fragment() {

    private var _binding: FragmentAddMoodBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefsManager: PreferencesManager
    private lateinit var moodAdapter: MoodLogAdapter
    private var selectedMood: String? = null
    private var selectedEmoji: String? = null
    private var editingLogId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddMoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefsManager = PreferencesManager(requireContext())
        setupMoodSelector()
        setupSaveButton()

        // Check if editing existing log
        arguments?.getString("logId")?.let {
            logId = it
            loadExistingLog(logId)
        }


    }

    private fun setupMoodSelector() {
        val moods = listOf(
            Pair("Energetic", "⚡"),
            Pair("Happy", "😊"),
            Pair("Calm", "😌"),
            Pair("Tired", "😴"),
            Pair("Stressed", "😰"),
            Pair("Sad", "😢")
        )
        moodAdapter = MoodAdapter(moods) { mood, emoji ->
            selectedMood = mood
            selectedEmoji = emoji
        }

        binding.rvMoodOptions.adapter = moodAdapter
    }

    private fun setupSaveButton() {
        binding.btnSaveMood.setOnClickListener {
            saveMoodLog()
        }
    }
    private fun saveMoodLog() {
        val mood = selectedMood
        val emoji = selectedEmoji
        val note = binding.etThoughts.text.toString()

        if (mood == null || emoji == null) {
            Toast.makeText(requireContext(), "Please select a mood", Toast.LENGTH_SHORT).show()
            return
        }
        val logId = arguments?.getString("logId")
        val timestamp = System.currentTimeMillis()
        val timeString = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(timestamp))

        if (logId != null) {
            // Update existing mood log
            val log = prefsManager.getMoodLogs().find { it.id == logId }
            log?.let {
                val updatedLog = it.copy(
                    mood = mood,
                    emoji = emoji,
                    note = note,
                    timestamp = timestamp,
                    timeString = timeString
                )
                prefsManager.updateMoodLog(updatedLog)
                Toast.makeText(requireContext(), "Mood updated: $mood", duration = Toast.LENGTH_SHORT).show()
            }
        } else {
            val moodLog = MoodLog(
                id = UUID.randomUUID().toString(),
                mood = mood,
                emoji = emoji,
                note = note,
                timestamp = timestamp,
                timeString = timeString
            )

            prefsManager.saveMoodLog(moodLog)
            Toast.makeText(requireContext(), "Mood saved successfully!", duration = Toast.LENGTH_SHORT).show()
        }

        findNavController().navigateUp()
    }

    private fun loadExistingLog(logId: String) {
        val log = prefsManager.getMoodLogs().find { it.id == logId }
        log?.let {
            selectedMood = it.mood
            selectedEmoji = it.emoji
            binding.etThoughts.setText(it.note)
            binding.btnAddMood.text = "Update Mood"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}