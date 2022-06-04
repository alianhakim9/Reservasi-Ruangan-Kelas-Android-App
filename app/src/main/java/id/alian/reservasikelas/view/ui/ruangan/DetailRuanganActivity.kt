package id.alian.reservasikelas.view.ui.ruangan

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import id.alian.reservasikelas.R
import id.alian.reservasikelas.databinding.ActivityDetailRuanganBinding
import id.alian.reservasikelas.service.model.BookRuangan
import id.alian.reservasikelas.view.callback.Constants.TAG
import id.alian.reservasikelas.view.callback.Resource
import id.alian.reservasikelas.view.callback.openActivity
import id.alian.reservasikelas.view.callback.shortSnackBar
import id.alian.reservasikelas.view.ui.reservasi.StatusReservasiActivity
import id.alian.reservasikelas.viewmodel.RuanganViewModel
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat

@AndroidEntryPoint
class DetailRuanganActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailRuanganBinding
    private val viewModel: RuanganViewModel by viewModels()
    private var idDosen = 0
    private var idMatkul = 0
    private var tanggal = ""
    private var jamAwal = ""
    private var jamAkhir = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRuanganBinding.inflate(layoutInflater)
        with(binding) {
            setContentView(root)
            toolbar.setNavigationOnClickListener {
                finish()
            }

            val kodeRuangan = intent.getStringExtra(getString(R.string.extra_kode_ruangan))
            val idRuangan = intent.getIntExtra(getString(R.string.extra_id_ruangan), 0)
            val idMahasiswa = viewModel.sharedPref.getInt(getString(R.string.extra_id_mahasiswa), 0)

            kodeRuanganTextView.text = kodeRuangan
            getDataDosen()
            getDataMataKuliah()

            matakuliahTextInputLayout.editText?.setOnClickListener {
                viewModel.getDataMataKuliah()
            }
            dosenTextInputLayout.editText?.setOnClickListener {
                viewModel.getDataDosen()
            }

            tanggalTextInputLayout.editText?.setOnClickListener {
                showDatePicker()
            }

            jamAwalTextInput.editText?.setOnClickListener {
                showTimePicker(
                    status = getString(R.string.title_jam_awal)
                )
            }

            jamAkhirTextInput.editText?.setOnClickListener {
                showTimePicker(
                    status = getString(R.string.title_jam_akhir)
                )
            }

            btnReservasi.setOnClickListener {
                Log.i(TAG, "onCreate: $idMahasiswa")
                val bookRuangan = BookRuangan(
                    idRuangan = idRuangan,
                    idMahasiswa = idMahasiswa,
                    idDosen = idDosen,
                    idMataKuliah = idMatkul,
                    tanggal = tanggal,
                    jamAwal = jamAwal,
                    jamAkhir = jamAkhir
                )
                viewModel.bookRuangan(bookRuangan)
            }
        }
        updateUIState()
    }

    private fun getDataDosen() {
        lifecycleScope.launchWhenCreated {
            viewModel.dosen.collectLatest {
                when (it) {
                    is Resource.Success -> {
                        hideLoading()
                        val list: MutableList<String> = mutableListOf()
                        val listId: MutableList<Int> = mutableListOf()
                        it.data?.data?.forEach { dosen ->
                            list.add(dosen.nama)
                            listId.add(dosen.id!!)
                        }
                        list.toList()
                        val newList: Array<String> = list.toTypedArray()
                        val newListId: Array<Int> = listId.toTypedArray()
                        customDialog(getString(R.string.title_pilih_dosen), newList, newListId)
                    }

                    is Resource.Loading -> {
                        showLoading()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun getDataMataKuliah() {
        lifecycleScope.launchWhenCreated {
            viewModel.mataKuliah.collectLatest {
                when (it) {
                    is Resource.Success -> {
                        hideLoading()
                        val list: MutableList<String> = mutableListOf()
                        val listId: MutableList<Int> = mutableListOf()
                        it.data?.data?.forEach { mataKuliah ->
                            list.add(mataKuliah.namaMataKuliah)
                            listId.add(mataKuliah.id!!)
                        }
                        list.toList()

                        val newList: Array<String> = list.toTypedArray()
                        val newListId: Array<Int> = listId.toTypedArray()

                        customDialog(getString(R.string.title_pilih_matkul), newList, newListId)
                    }

                    is Resource.Loading -> {
                        showLoading()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun customDialog(title: String, data: Array<String>, id: Array<Int>) {
        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setItems(data) { _, which ->
                // Respond to item chosen
                when (title) {
                    getString(R.string.title_pilih_dosen) -> {
                        Log.i(TAG, "customDialog: ${id[which]}")
                        idDosen = id[which]
                        binding.dosenTextInputLayout.editText?.setText(data[which])
                    }
                    getString(R.string.title_pilih_matkul) -> {
                        idMatkul = id[which]
                        binding.matakuliahTextInputLayout.editText?.setText(data[which])
                    }
                }
            }
            .show()
    }

    private fun showLoading() {
        binding.root.shortSnackBar(getString(R.string.title_harap_tunggu), action = {
            setAction(getString(R.string.title_ok)) {
                this.dismiss()
            }
        })
    }

    private fun hideLoading() {
        with(binding) {
            root.isEnabled = true
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun showDatePicker() {
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.title_pilih_tanggal))
            .build()
        picker.addOnPositiveButtonClickListener {
            val simpleDateFormat = SimpleDateFormat(getString(R.string.date_format))
            tanggal = simpleDateFormat.format(it)
            binding.tanggalTextInputLayout.editText?.setText(tanggal)
        }
        picker.show(supportFragmentManager, TAG)
    }

    private fun showTimePicker(status: String) {
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(6)
            .setMinute(10)
            .setTitleText(getString(R.string.title_pilih_jam, status))
            .build()

        timePicker.addOnPositiveButtonClickListener {
            when (status) {
                getString(R.string.title_jam_awal) -> {
                    jamAwal = getString(R.string.clock_format, timePicker.hour, timePicker.minute)
                    binding.jamAwalTextInput.editText?.setText(jamAwal)
                }

                getString(R.string.title_jam_akhir) -> {
                    jamAkhir = getString(R.string.clock_format, timePicker.hour, timePicker.minute)
                    binding.jamAkhirTextInput.editText?.setText(jamAkhir)
                }
            }
        }

        timePicker.show(supportFragmentManager, TAG)
    }

    private fun updateUIState() {
        lifecycleScope.launchWhenCreated {
            viewModel.bookRuangan.collectLatest {
                when (it) {
                    is Resource.Success -> {
                        stopReservation()
                        binding.root.shortSnackBar(getString(R.string.title_success_reservation),
                            action = {
                                setAction(getString(R.string.title_ok)) {
                                    openActivity(StatusReservasiActivity::class.java)
                                    finish()
                                }
                                setBackgroundTint(getCustomColor(R.color.success))
                            })
                    }

                    is Resource.Loading -> {
                        isReservation()
                    }

                    is Resource.Error -> {
                        stopReservation()
                        binding.root.shortSnackBar(it.message.toString())
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun isReservation() {
        with(binding.btnReservasi) {
            text = getString(R.string.title_sedang_reservasi)
            isEnabled = false
        }
    }

    private fun stopReservation() {
        with(binding.btnReservasi) {
            text = getString(R.string.title_reservasi)
            isEnabled = true
        }
    }

    private fun getCustomColor(color: Int): Int {
        return ResourcesCompat.getColor(resources, color, theme)
    }

}