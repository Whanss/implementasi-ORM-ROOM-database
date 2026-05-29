package com.ikhwan.roomdatabse

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ikhwan.roomdatabse.data.AppDatabase
import com.ikhwan.roomdatabse.data.Komputer
import com.ikhwan.roomdatabse.data.StatusKomputer
import com.ikhwan.roomdatabse.databinding.ActivityMainBinding
import com.ikhwan.roomdatabse.databinding.DialogAddKomputerBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: KomputerAdapter
    private val database by lazy { AppDatabase.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        binding.fabAdd.setOnClickListener {
            showKomputerDialog(null)
        }

        observeData()
    }

    private fun setupRecyclerView() {
        adapter = KomputerAdapter(
            onEdit = { komputer -> showKomputerDialog(komputer) },
            onDelete = { komputer -> confirmDelete(komputer) }
        )
        binding.rvKomputer.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            database.komputerDao().getAllKomputer().collect { list ->
                adapter.submitList(list)
            }
        }
    }

    private fun showKomputerDialog(komputer: Komputer?) {
        val dialogBinding = DialogAddKomputerBinding.inflate(LayoutInflater.from(this))
        val builder = AlertDialog.Builder(this).setView(dialogBinding.root)
        val alertDialog = builder.create()

        if (komputer != null) {
            dialogBinding.tvTitle.text = "Edit Data Komputer"
            dialogBinding.etNamaLab.setText(komputer.nama_lab)
            dialogBinding.etKodeKomputer.setText(komputer.kode_komputer)
            dialogBinding.etMerkCpu.setText(komputer.merk_cpu)
            dialogBinding.etMerkMonitor.setText(komputer.merk_monitor)
            dialogBinding.etUkuranMonitor.setText(komputer.ukuran_monitor)
            dialogBinding.etSistemOperasi.setText(komputer.sistem_operasi)
            dialogBinding.etKeterangan.setText(komputer.keterangan)
            
            when (komputer.status) {
                StatusKomputer.BAGUS -> dialogBinding.toggleGroupStatus.check(R.id.btnBagus)
                StatusKomputer.MAINTENANCE -> dialogBinding.toggleGroupStatus.check(R.id.btnMaintenance)
                StatusKomputer.RUSAK -> dialogBinding.toggleGroupStatus.check(R.id.btnRusak)
            }
        } else {
            dialogBinding.toggleGroupStatus.check(R.id.btnBagus)
        }

        dialogBinding.btnBatal.setOnClickListener {
            alertDialog.dismiss()
        }

        dialogBinding.btnSimpan.setOnClickListener {
            val namaLab = dialogBinding.etNamaLab.text.toString().trim()
            val kode = dialogBinding.etKodeKomputer.text.toString().trim()
            val cpu = dialogBinding.etMerkCpu.text.toString().trim()
            val monitor = dialogBinding.etMerkMonitor.text.toString().trim()
            val ukuran = dialogBinding.etUkuranMonitor.text.toString().trim()
            val os = dialogBinding.etSistemOperasi.text.toString().trim()
            val ket = dialogBinding.etKeterangan.text.toString().trim()
            
            val status = when (dialogBinding.toggleGroupStatus.checkedButtonId) {
                R.id.btnBagus -> StatusKomputer.BAGUS
                R.id.btnMaintenance -> StatusKomputer.MAINTENANCE
                R.id.btnRusak -> StatusKomputer.RUSAK
                else -> StatusKomputer.BAGUS
            }

            if (namaLab.isEmpty() || kode.isEmpty()) {
                Toast.makeText(this, "Nama Lab dan Kode harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newKomputer = if (komputer == null) {
                Komputer(
                    nama_lab = namaLab,
                    kode_komputer = kode,
                    merk_cpu = cpu,
                    merk_monitor = monitor,
                    ukuran_monitor = ukuran,
                    sistem_operasi = os,
                    status = status,
                    keterangan = ket
                )
            } else {
                komputer.copy(
                    nama_lab = namaLab,
                    kode_komputer = kode,
                    merk_cpu = cpu,
                    merk_monitor = monitor,
                    ukuran_monitor = ukuran,
                    sistem_operasi = os,
                    status = status,
                    keterangan = ket,
                    updated_at = System.currentTimeMillis()
                )
            }

            lifecycleScope.launch {
                if (komputer == null) {
                    database.komputerDao().insert(newKomputer)
                } else {
                    database.komputerDao().update(newKomputer)
                }
                alertDialog.dismiss()
            }
        }

        alertDialog.show()
    }

    private fun confirmDelete(komputer: Komputer) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Data")
            .setMessage("Apakah Anda yakin ingin menghapus data komputer ${komputer.kode_komputer}?")
            .setPositiveButton("Hapus") { _, _ ->
                lifecycleScope.launch {
                    database.komputerDao().delete(komputer)
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
