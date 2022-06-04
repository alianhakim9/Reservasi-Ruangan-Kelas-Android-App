package id.alian.reservasikelas.view.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import id.alian.reservasikelas.databinding.ActivityPilihanLoginBinding
import id.alian.reservasikelas.view.callback.openActivity
import id.alian.reservasikelas.view.ui.dosen.LoginDosenActivity
import id.alian.reservasikelas.view.ui.mahasiswa.LoginMahasiswaActivity
import id.alian.reservasikelas.viewmodel.IsLoginViewModel


@AndroidEntryPoint
class PilihanLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPilihanLoginBinding

    private val viewModel: IsLoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPilihanLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {
            btnLoginMahasiswa.setOnClickListener {
                openActivity(LoginMahasiswaActivity::class.java)
            }

            btnLoginDosen.setOnClickListener {
                openActivity(LoginDosenActivity::class.java)
            }
        }

        viewModel.isLogin.observe(this) {
            openActivity(HomeActivity::class.java).also {
                finish()
            }
        }
    }
}