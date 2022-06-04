package id.alian.reservasikelas.view.ui.dosen

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import dagger.hilt.android.AndroidEntryPoint
import id.alian.reservasikelas.R
import id.alian.reservasikelas.databinding.ActivityProfileDosenBinding
import id.alian.reservasikelas.view.callback.Resource
import id.alian.reservasikelas.view.callback.openActivity
import id.alian.reservasikelas.view.callback.shortSnackBar
import id.alian.reservasikelas.viewmodel.DosenViewModel

@AndroidEntryPoint
class ProfileDosenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileDosenBinding

    private val viewModel: DosenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileDosenBinding.inflate(layoutInflater)
        with(binding) {
            setContentView(root)
            toolbar.setNavigationOnClickListener {
                finish()
            }
            btnEditProfile.setOnClickListener {
                openActivity(EditProfileDosenActivity::class.java)
            }

            toolbar.setNavigationOnClickListener { finish() }
        }

        viewModel.getProfile()

        viewModel.profile.observe(this) {
            when (it) {
                is Resource.Success -> {
                    hideLoading()
                    with(binding) {
                        with(it.data?.data) {
                            namaTextView.text = this?.nama
                            nidnTextView.text = this?.nidn
                        }
                    }
                }

                is Resource.Loading -> {
                    showLoading()
                }

                is Resource.Error -> {
                    hideLoading()
                    binding.root.shortSnackBar(it.message.toString(), action = {
                        this.setBackgroundTint(getCustomColor(R.color.md_theme_light_error))
                    })
                }
                else -> Unit
            }
        }
    }

    private fun showLoading() {
        with(binding) {
            namaTextView.text = getString(R.string.title_loading)
            nidnTextView.visibility = View.GONE
            btnEditProfile.isEnabled = false
        }
    }

    private fun hideLoading() {
        with(binding) {
            namaTextView.text = getString(R.string.title_nama)
            nidnTextView.visibility = View.VISIBLE
            btnEditProfile.isEnabled = true
        }
    }

    private fun getCustomColor(color: Int): Int {
        return ResourcesCompat.getColor(resources, color, theme)
    }
}