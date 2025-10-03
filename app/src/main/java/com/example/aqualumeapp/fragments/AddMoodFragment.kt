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

class AddMoodFragment : Fragment() {

    private var _binding: FragmentAddMoodBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefsManager: PreferencesManager
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

        // Check if editing existing log
        arguments?.getString("logId")?.let {
            editingLogId = it
            loadExistingLog(it)
        }

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // Mood selection
        binding.cardHappy.setOnClickListener {
            selectMood("happy", "😊")
        }

        binding.cardSad.setOnClickListener {
            selectMood("sad", "😢")
        }

        binding.cardAngry.setOnClickListener {
            selectMood("angry", "😠")
        }

        binding.cardCalm.setOnClickListener {
            selectMood("calm", "😌")
        }

        binding.cardNeutral.setOnClickListener {
            selectMood("neutral", "😐")
        }

        binding.cardEnergetic.setOnClickListener {
            selectMood("energetic", "😄")
        }

        binding.cardTired.setOnClickListener {
            selectMood("tired", "😫")
        }

        binding.cardStressed.setOnClickListener {
            selectMood("stressed", "😰")
        }

        binding.btnAddMood.setOnClickListener {
            addOrUpdateMood()
        }
    }

    private fun selectMood(mood: String, emoji: String) {
        selectedMood = mood
        selectedEmoji = emoji

        // Reset all card borders
        binding.cardHappy.strokeWidth = 2
        binding.cardSad.strokeWidth = 2
        binding.cardAngry.strokeWidth = 2
        binding.cardCalm.strokeWidth = 2
        binding.cardNeutral.strokeWidth = 2
        binding.cardEnergetic.strokeWidth = 2
        binding.cardTired.strokeWidth = 2
        binding.cardStressed.strokeWidth = 2

        // Highlight selected card
        when (mood) {
            "happy" -> binding.cardHappy.strokeWidth = 4
            "sad" -> binding.cardSad.strokeWidth = 4
            "angry" -> binding.cardAngry.strokeWidth = 4
            "calm" -> binding.cardCalm.strokeWidth = 4
            "neutral" -> binding.cardNeutral.strokeWidth = 4
            "energetic" -> binding.cardEnergetic.strokeWidth = 4
            "tired" -> binding.cardTired.strokeWidth = 4
            "stressed" -> binding.cardStressed.strokeWidth = 4
        }
    }

    private fun addOrUpdateMood() {
        if (selectedMood == null || selectedEmoji == null) {
            Toast.makeText(requireContext(), "Please select a mood", Toast.LENGTH_SHORT).show()
            return
        }

        val note = binding.etThoughts.text.toString()
        val timeFormat = SimpleDateFormat("h.mm a", Locale.getDefault())
        val timestamp = System.currentTimeMillis()
        val timeString = timeFormat.format(Date(timestamp))

        if (editingLogId != null) {
            val updatedLog = MoodLog(
                id = editingLogId!!,
                mood = selectedMood!!,
                emoji = selectedEmoji!!,
                note = note,
                timestamp = timestamp,
                timeString = timeString
            )
            prefsManager.updateMoodLog(updatedLog)
            Toast.makeText(requireContext(), "Mood updated!", Toast.LENGTH_SHORT).show()
        } else {
            val moodLog = MoodLog(
                id = UUID.randomUUID().toString(),
                mood = selectedMood!!,
                emoji = selectedEmoji!!,
                note = note,
                timestamp = timestamp,
                timeString = timeString
            )
            prefsManager.saveMoodLog(moodLog)
            Toast.makeText(requireContext(), "Mood saved successfully!", Toast.LENGTH_SHORT).show()
        }

        findNavController().navigateUp()
    }

    private fun loadExistingLog(logId: String) {
        val log = prefsManager.getMoodLogs().find { it.id == logId }
        log?.let {
            selectMood(it.mood, it.emoji)
            binding.etThoughts.setText(it.note)
            binding.btnAddMood.text = "Update Mood"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}