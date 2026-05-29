package com.ikhwan.roomdatabse

import android.content.res.ColorStateList
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ikhwan.roomdatabse.data.Komputer
import com.ikhwan.roomdatabse.data.StatusKomputer
import com.ikhwan.roomdatabse.databinding.ItemKomputerBinding

class KomputerAdapter(
    private val onEdit: (Komputer) -> Unit,
    private val onDelete: (Komputer) -> Unit
) : ListAdapter<Komputer, KomputerAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(val binding: ItemKomputerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemKomputerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val context = holder.itemView.context
        
        holder.binding.apply {
            tvNamaLab.text = item.nama_lab
            tvKodeKomputer.text = item.kode_komputer
            
            // Set Spesifikasi Teknis
            tvCpu.text = ": ${item.merk_cpu}"
            tvMonitor.text = ": ${item.merk_monitor} (${item.ukuran_monitor})"
            tvOs.text = ": ${item.sistem_operasi}"

            // Tampilkan keterangan jika tersedia
            if (item.keterangan.isNotEmpty()) {
                tvKeteranganLabel.visibility = View.VISIBLE
                tvKeterangan.visibility = View.VISIBLE
                tvKeterangan.text = item.keterangan
            } else {
                tvKeteranganLabel.visibility = View.GONE
                tvKeterangan.visibility = View.GONE
            }

            // Status Chip
            tvStatus.text = item.status.value.uppercase()
            val (bgColor, textColor) = when (item.status) {
                StatusKomputer.BAGUS -> R.color.status_bagus_bg to R.color.status_bagus_text
                StatusKomputer.MAINTENANCE -> R.color.status_maintenance_bg to R.color.status_maintenance_text
                StatusKomputer.RUSAK -> R.color.status_rusak_bg to R.color.status_rusak_text
            }
            
            tvStatus.chipBackgroundColor = ColorStateList.valueOf(ContextCompat.getColor(context, bgColor))
            tvStatus.setTextColor(ContextCompat.getColor(context, textColor))

            // Waktu Update
            tvDate.text = "Update: " + DateUtils.getRelativeTimeSpanString(
                item.updated_at,
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS
            )
            
            btnEdit.setOnClickListener { onEdit(item) }
            btnDelete.setOnClickListener { onDelete(item) }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Komputer>() {
        override fun areItemsTheSame(oldItem: Komputer, newItem: Komputer) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Komputer, newItem: Komputer) = oldItem == newItem
    }
}
