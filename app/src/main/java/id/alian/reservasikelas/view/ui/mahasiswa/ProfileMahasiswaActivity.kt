package id.alian.reservasikelas.view.ui.mahasiswa

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import id.alian.reservasikelas.R
import id.alian.reservasikelas.databinding.ActivityProfileMahasiswaBinding
import id.alian.reservasikelas.view.callback.Resource
import id.alian.reservasikelas.view.callback.shortSnackBar
import id.alian.reservasikelas.viewmodel.MahasiswaViewModel
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProfileMahasiswaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileMahasiswaBinding

    private val viewModel: MahasiswaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileMahasiswaBinding.inflate(layoutInflater)
        viewModel.getProfile()
        with(binding) {
            setContentView(root)
            toolbar.setNavigationOnClickListener {
                finish()
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.profile.collectLatest {
                when (it) {
                    is Resource.Success -> {
                        hideLoading()
                        with(binding) {
                            with(it.data?.data) {
                                this?.forEach { mhs ->
                                    namaTextView.text = mhs.nama
                                    nimTextView.text = mhs.nim
                                    kelasTextView.text =
                                        getString(R.string.title_kelas, ": ${mhs.kelas.kodeKelas}")
                                    jurusanTextView.text = getString(R.string.title_jurusan,
                                        ": ${mhs.jurusan.kodeJurusan}")
                                    tahunAngkatanTextView.text =
                                        getString(R.string.title_tahun_angkatan,
                                            ": ${mhs.tahunAngkatan}")
                                }
                            }
                        }
                    }

                    is Resource.Loading -> {
                        showLoading()
                    }

                    is Resource.Error -> {
                        hideLoading()
                        binding.root.shortSnackBar(it.message.toString(), action = {
                            this.setBackgroundTint(getCustomColor(R.color.md_theme_dark_onError))
                        })
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun showLoading() {
        with(binding) {
            namaTextView.text = getString(R.string.title_loading)
            nimTextView.visibility = View.GONE
            kelasTextView.visibility = View.GONE
            jurusanTextView.visibility = View.GONE
            tahunAngkatanTextView.visibility = View.GONE
            btnEditProfile.isEnabled = false
        }
    }

    private fun hideLoading() {
        with(binding) {
            namaTextView.text = getString(R.string.title_nama)
            nimTextView.visibility = View.VISIBLE
            kelasTextView.visibility = View.VISIBLE
            jurusanTextView.visibility = View.VISIBLE
            tahunAngkatanTextView.visibility = View.VISIBLE
            btnEditProfile.isEnabled = true
        }
    }

    private fun getCustomColor(color: Int): Int {
        return ResourcesCompat.getColor(resources, color, theme)
    }
}