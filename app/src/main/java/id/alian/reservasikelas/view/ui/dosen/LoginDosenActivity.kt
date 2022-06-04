package id.alian.reservasikelas.view.ui.dosen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import id.alian.reservasikelas.R
import id.alian.reservasikelas.databinding.ActivityLoginDosenBinding
import id.alian.reservasikelas.view.callback.Resource
import id.alian.reservasikelas.view.callback.openActivity
import id.alian.reservasikelas.view.callback.shortSnackBar
import id.alian.reservasikelas.view.ui.HomeActivity
import id.alian.reservasikelas.view.ui.PilihanLoginActivity
import id.alian.reservasikelas.viewmodel.DosenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LoginDosenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginDosenBinding

    private val viewModel: DosenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginDosenBinding.inflate(layoutInflater)
        with(binding) {
            setContentView(root)

            btnLogin.setOnClickListener {
                viewModel.login(
                    nidn = nidnTextInput.editText?.text.toString(),
                    password = passwordTextInput.editText?.text.toString()
                )
            }

            btnPulihkan.setOnClickListener {
                // open pulihkan password activity
            }
        }

        updateUIState()
    }

    private fun updateUIState() {
        lifecycleScope.launchWhenCreated {
            viewModel.login.collectLatest {
                when (it) {
                    is Resource.Success -> {
                        hideLoading()
                        viewModel.sharedPref.edit()
                            .putInt(getString(R.string.extra_id_dosen), it.data?.data?.id!!)
                            .apply()
                        viewModel.sharedPref.edit().putString("nidn", it.data.data.nidn).apply()
                        binding.root.shortSnackBar(it.data.message, action = {
                            this.setBackgroundTint(getCustomColor(R.color.success))
                        })
                        lifecycleScope.launchWhenCreated {
                            delay(1000)
                        }
                        openActivity(HomeActivity::class.java).also {
                            finish()
                        }
                    }

                    is Resource.Loading -> {
                        showLoading()
                    }

                    is Resource.Error -> {
                        hideLoading()
                        binding.root.shortSnackBar(it.message.toString(), action = {
                            this.setBackgroundTint(getCustomColor(androidx.appcompat.R.color.error_color_material_dark))
                        })
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun showLoading() {
        with(binding) {
            btnLogin.isEnabled = false
            btnLogin.text = getString(R.string.title_sedang_masuk)
        }
    }

    private fun hideLoading() {
        with(binding) {
            btnLogin.isEnabled = true
            btnLogin.text = getString(R.string.title_masuk)
        }
    }

    private fun getCustomColor(color: Int): Int {
        return ResourcesCompat.getColor(resources, color, theme)
    }

    override fun onBackPressed() {
        openActivity(PilihanLoginActivity::class.java).also {
            finish()
        }
        super.onBackPressed()
    }
}