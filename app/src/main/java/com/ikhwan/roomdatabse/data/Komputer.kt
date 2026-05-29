package com.ikhwan.roomdatabse.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class StatusKomputer(val value: String) {
    BAGUS("bagus"),
    MAINTENANCE("maintenance"),
    RUSAK("rusak");

    companion object {
        fun fromValue(value: String): StatusKomputer {
            return entries.firstOrNull { it.value == value } ?: BAGUS
        }
    }
}

@Entity(tableName = "komputers")
data class Komputer(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nama_lab: String,
    val kode_komputer: String,
    val merk_cpu: String,
    val merk_monitor: String,
    val ukuran_monitor: String,
    val sistem_operasi: String,
    val status: StatusKomputer,
    val keterangan: String,
    val created_at: Long = System.currentTimeMillis(),
    var updated_at: Long = System.currentTimeMillis()
)
