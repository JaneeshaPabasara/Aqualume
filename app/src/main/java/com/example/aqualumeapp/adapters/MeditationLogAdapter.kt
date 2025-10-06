package com.example.aqualumeapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aqualumeapp.databinding.ItemMeditationLogBinding
import com.example.aqualumeapp.models.MeditationLog
import java.text.SimpleDateFormat
import java.util.*
import com.example.aqualumeapp.R

class MeditationLogAdapter(
    private val onEditClick: (MeditationLog) -> Unit,
    private val onDeleteClick: (MeditationLog) -> Unit
) : ListAdapter<MeditationLog, MeditationLogAdapter.MeditationLogViewHolder>(MeditationLogDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeditationLogViewHolder {
        val binding = ItemMeditationLogBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MeditationLogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MeditationLogViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MeditationLogViewHolder(
        private val binding: ItemMeditationLogBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(meditationLog: MeditationLog) {
            binding.tvDuration.text = binding.root.context.getString(
                R.string.time_duration,
                meditationLog.duration
            )
            // Format time from timestamp
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            binding.tvTime.text = timeFormat.format(Date(meditationLog.timestamp))

            // Show date if different from today
            val today = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val logDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(meditationLog.timestamp))

            if (today != logDate) {
                binding.tvDate.visibility = android.view.View.VISIBLE
                binding.tvDate.text = logDate
            } else {
                binding.tvDate.visibility = android.view.View.GONE
            }

            binding.ivEdit.setOnClickListener {
                onEditClick(meditationLog)
            }

            binding.ivDelete.setOnClickListener {
                onDeleteClick(meditationLog)
            }
        }
    }


    class MeditationLogDiffCallback : DiffUtil.ItemCallback<MeditationLog>() {

        override fun areItemsTheSame(oldItem: MeditationLog, newItem: MeditationLog): Boolean {
            return oldItem.id == newItem.id
        }


        override fun areContentsTheSame(oldItem: MeditationLog, newItem: MeditationLog): Boolean {
            return oldItem == newItem
        }
    }
}