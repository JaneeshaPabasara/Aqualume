package com.example.aqualumeapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aqualumeapp.databinding.ItemMoodLogBinding
import com.example.aqualumeapp.models.MoodLog
import java.text.SimpleDateFormat
import java.util.*

class MoodLogAdapter(
    private val onEditClick: (MoodLog) -> Unit,
    private val onDeleteClick: (MoodLog) -> Unit
) : ListAdapter<MoodLog, MoodLogAdapter.MoodLogViewHolder>(MoodLogDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodLogViewHolder {
        val binding = ItemMoodLogBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MoodLogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoodLogViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MoodLogViewHolder(
        private val binding: ItemMoodLogBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(moodLog: MoodLog) {
            // Set mood emoji image
            binding.tvEmoji.setImageResource(moodLog.moodDrawable)

            // Set mood name with capitalization
            binding.tvMood.text = moodLog.mood.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }

            // Format and set time
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            binding.tvTime.text = timeFormat.format(Date(moodLog.timestamp))

            // Show date if different from today
            val today = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val logDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(moodLog.timestamp))

            if (today != logDate) {
                binding.tvDate.visibility = android.view.View.VISIBLE
                binding.tvDate.text = logDate
            } else {
                binding.tvDate.visibility = android.view.View.GONE
            }

            // Show note if available
            if (moodLog.note.isNotEmpty()) {
                binding.tvNote.visibility = android.view.View.VISIBLE
                binding.tvNote.text = moodLog.note
            } else {
                binding.tvNote.visibility = android.view.View.GONE
            }

            // Edit button click
            binding.ivEdit.setOnClickListener {
                onEditClick(moodLog)
            }

            // Delete button click
            binding.ivDelete.setOnClickListener {
                onDeleteClick(moodLog)
            }
        }
    }

    class MoodLogDiffCallback : DiffUtil.ItemCallback<MoodLog>() {
        override fun areItemsTheSame(oldItem: MoodLog, newItem: MoodLog): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MoodLog, newItem: MoodLog): Boolean {
            return oldItem == newItem
        }
    }
}