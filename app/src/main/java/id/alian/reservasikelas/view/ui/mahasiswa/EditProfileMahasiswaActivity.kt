package id.alian.reservasikelas.view.ui.mahasiswa

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import id.alian.reservasikelas.R
import id.alian.reservasikelas.databinding.ActivityEditProfileMahasiswaBinding
import id.alian.reservasikelas.view.callback.Resource
import id.alian.reservasikelas.view.callback.shortSnackBar
import id.alian.reservasikelas.viewmodel.MahasiswaViewModel
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class EditProfileMahasiswaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileMahasiswaBinding
    private val viewModel: MahasiswaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileMahasiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updateUIState()
        viewModel.getProfile()

        with(binding) {
            btnSave.setOnClickListener {
                viewModel.editProfile(
                    email = emailTextInput.editText?.text.toString(),
                    password = newPasswordTextInput.editText?.text.toString(),
                    confirmationPassword = confirmationPasswordTextInput.editText?.text.toString()
                )
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        lifecycleScope.launchWhenCreated {
            viewModel.profile.collectLatest {
                when (it) {
                    is Resource.Success -> {
                        binding.emailTextInput.isEnabled = true
                        it.data?.data?.forEach { mhs ->
                            binding.emailTextInput.editText?.setText(mhs.email)
                        }
                    }

                    is Resource.Loading -> {
                        binding.emailTextInput.isEnabled = false
                    }
                    else -> Unit
                }
            }
        }

    }

    private fun updateUIState() {
        lifecycleScope.launchWhenCreated {
            viewModel.update.collectLatest {
                when (it) {
                    is Resource.Success -> {
                        hideLoading()
                        binding.root.shortSnackBar(getString(R.string.title_update_success),
                            action = {
                                setBackgroundTint(getCustomColor(R.color.success))
                                setAction(getString(R.string.title_ok)) {
                                    finish()
                                }
                            })
                    }

                    is Resource.Loading -> {
                        showLoading()
                    }

                    is Resource.Error -> {
                        hideLoading()
                        binding.root.shortSnackBar(it.message.toString())
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun showLoading() {
        with(binding) {
            btnSave.isEnabled = false
            btnSave.text = getString(R.string.title_loading)
        }
    }

    private fun hideLoading() {
        with(binding) {
            btnSave.isEnabled = true
            btnSave.text = getString(R.string.title_simpan)
        }
    }

    private fun getCustomColor(color: Int): Int {
        return ResourcesCompat.getColor(resources, color, theme)
    }
}