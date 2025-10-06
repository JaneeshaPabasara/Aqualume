package com.example.aqualumeapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aqualumeapp.databinding.ItemWaterLogBinding
import com.example.aqualumeapp.models.WaterLog
import java.text.SimpleDateFormat
import java.util.*

class WaterLogAdapter(
    private val onEditClick: (WaterLog) -> Unit,
    private val onDeleteClick: (WaterLog) -> Unit
) : ListAdapter<WaterLog, WaterLogAdapter.WaterLogViewHolder>(WaterLogDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaterLogViewHolder {
        val binding = ItemWaterLogBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WaterLogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WaterLogViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class WaterLogViewHolder(
        private val binding: ItemWaterLogBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(waterLog: WaterLog) {
            binding.tvAmount.text = "${waterLog.amount}ml"
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            binding.tvTime.text = timeFormat.format(Date(waterLog.timestamp))

            // Show date if different from today
            val today = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val logDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(waterLog.timestamp))

            if (today != logDate) {
                binding.tvDate.visibility = android.view.View.VISIBLE
                binding.tvDate.text = logDate
            } else {
                binding.tvDate.visibility = android.view.View.GONE
            }

            binding.ivEdit.setOnClickListener {
                onEditClick(waterLog)
            }

            binding.ivDelete.setOnClickListener {
                onDeleteClick(waterLog)
            }
        }
    }

    class WaterLogDiffCallback : DiffUtil.ItemCallback<WaterLog>() {
        override fun areItemsTheSame(oldItem: WaterLog, newItem: WaterLog): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: WaterLog, newItem: WaterLog): Boolean {
            return oldItem == newItem
        }
    }
}