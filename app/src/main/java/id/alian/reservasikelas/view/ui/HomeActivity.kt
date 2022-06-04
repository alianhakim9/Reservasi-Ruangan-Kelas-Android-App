package id.alian.reservasikelas.view.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import id.alian.reservasikelas.R
import id.alian.reservasikelas.databinding.ActivityHomeBinding
import id.alian.reservasikelas.view.callback.openActivity
import id.alian.reservasikelas.view.ui.dosen.ProfileDosenActivity
import id.alian.reservasikelas.view.ui.mahasiswa.ProfileMahasiswaActivity
import id.alian.reservasikelas.view.ui.reservasi.StatusReservasiActivity
import id.alian.reservasikelas.view.ui.ruangan.ListRuanganActivity
import id.alian.reservasikelas.viewmodel.IsLoginViewModel

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val viewModel: IsLoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        with(binding) {
            setContentView(root)

            btnProfile.setOnClickListener {
                viewModel.profile()
            }

            btnListRuangan.setOnClickListener {
                openActivity(ListRuanganActivity::class.java)
            }

            btnStatusReservasi.setOnClickListener {
                openActivity(StatusReservasiActivity::class.java)
            }

            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_logout -> {
                        showDialog()
                        true
                    }
                    else -> false
                }
            }
        }

        gotoProfile()
    }

    private fun gotoProfile() {
        viewModel.profileDosen.observe(this) {
            openActivity(ProfileDosenActivity::class.java)
        }

        viewModel.profileMhs.observe(this) {
            openActivity(ProfileMahasiswaActivity::class.java)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_homepage, menu)
        return true
    }

    private fun showDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.title_logout))
            .setMessage(resources.getString(R.string.title_logout_supporting_text))
            .setNegativeButton(resources.getString(R.string.title_cancel)) { dialog, _ ->
                // Respond to negative button press
                dialog.cancel()
            }
            .setPositiveButton(resources.getString(R.string.title_accept)) { _, _ ->
                // Respond to positive button press
                viewModel.logout()
                openActivity(PilihanLoginActivity::class.java).also {
                    finish()
                }
            }
            .show()
    }
}