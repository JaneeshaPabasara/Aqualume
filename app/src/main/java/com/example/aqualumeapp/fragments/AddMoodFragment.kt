// fragments/AddMoodFragment.kt
package com.example.aqualumeapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aqualumeapp.R
import com.example.aqualumeapp.adapters.MoodLogAdapter
import com.example.aqualumeapp.models.MoodLog

import com.example.aqualumeapp.utils.PreferencesManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class AddMoodFragment : Fragment() {

    private lateinit var rvMoodOptions: RecyclerView
    private lateinit var btnSaveMood: Button
    private lateinit var etNote: EditText
    private lateinit var prefsManager: PreferencesManager
    private lateinit var moodAdapter: MoodLogAdapter

    private var selectedMood: MoodLog? = null
    private var mood: String = ""
    private var emoji: String = ""
    private var moodDrawable: Int = 0
    private var note: String = ""
    private var timestamp: Long = System.currentTimeMillis()
    private var timeString: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    private var log: MoodLog? = null
    private var logId: String? = null

    // Define your moods with images
    private val moods = listOf(
        Mood("Happy", R.drawable.hapimg, "😊"),
        Mood("Sad", R.drawable.sadimg, "😢"),
        Mood("Angry", R.drawable.angimg, "😠"),
        Mood("Calm", R.drawable.calmimg, "😌"),
        Mood("Anxious", R.drawable.scaimg, "😰"),
        Mood("Excited", R.drawable.eneimg, "🤗"),
        Mood("Tired", R.drawable.tiredimg, "😴"),
        Mood("Neutral", R.drawable.neimg, "😌")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_mood, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefsManager = PreferencesManager(requireContext())

        // Initialize views
        rvMoodOptions = view.findViewById(R.id.tv_emoji)
        btnSaveMood = view.findViewById(R.id.btn_add_mood)
        etNote = view.findViewById(R.id.et_thoughts)

        setupRecyclerView()
        setupSaveButton()
    }

    private fun setupRecyclerView() {
        moodAdapter = MoodLogAdapter(moods) { selectedMoodItem ->
            selectedMood = selectedMoodItem
            mood = selectedMoodItem.name
            emoji = selectedMoodItem.emoji
            moodDrawable = selectedMoodItem.drawableRes
        }

        rvMoodOptions.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = moodAdapter
        }
    }

    private fun setupSaveButton() {
        btnSaveMood.setOnClickListener {
            note = etNote.text.toString()

            if (selectedMood == null) {
                Toast.makeText(requireContext(), "Please select a mood", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            saveMoodLog()
        }
    }

    private fun saveMoodLog() {
        log?.let {
            val updatedLog = it.copy(
                mood = mood,
                emoji = emoji,
                moodDrawable = moodDrawable,
                note = note,
                timestamp = timestamp,
                timeString = timeString
            )

            prefsManager.updateMoodLog(logId = updatedLog.id, log = updatedLog)
            Toast.makeText(requireContext(), "Mood updated: $mood", Toast.LENGTH_SHORT).show()
        } ?: run {
            val moodLog = MoodLog(
                id = UUID.randomUUID().toString(),
                mood = mood,
                emoji = emoji,
                moodDrawable = moodDrawable,
                note = note,
                timestamp = timestamp,
                timeString = timeString
            )

            prefsManager.saveMoodLog(moodLog)
            Toast.makeText(requireContext(), "Mood saved successfully!", Toast.LENGTH_SHORT).show()
        }

        // Navigate back or clear form
        activity?.onBackPressed()
    }
}