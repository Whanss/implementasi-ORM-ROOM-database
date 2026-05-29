package com.ikhwan.roomdatabse

import android.content.res.ColorStateList
import android.view.LayoutInflater
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
        holder.binding.apply {
            tvNamaLab.text = item.nama_lab
            tvKodeKomputer.text = item.kode_komputer
            tvSpecs.text = "${item.merk_cpu} | ${item.merk_monitor} (${item.ukuran_monitor}) | ${item.sistem_operasi}"
            tvStatus.text = item.status.value
            val context = root.context
            val (statusBackground, statusText) = when (item.status) {
                StatusKomputer.BAGUS -> R.color.status_bagus_bg to R.color.status_bagus_text
                StatusKomputer.MAINTENANCE -> R.color.status_maintenance_bg to R.color.status_maintenance_text
                StatusKomputer.RUSAK -> R.color.status_rusak_bg to R.color.status_rusak_text
            }
            tvStatus.chipBackgroundColor = ColorStateList.valueOf(
                ContextCompat.getColor(context, statusBackground)
            )
            tvStatus.setTextColor(ContextCompat.getColor(context, statusText))
            
            btnEdit.setOnClickListener { onEdit(item) }
            btnDelete.setOnClickListener { onDelete(item) }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Komputer>() {
        override fun areItemsTheSame(oldItem: Komputer, newItem: Komputer) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Komputer, newItem: Komputer) = oldItem == newItem
    }
}
