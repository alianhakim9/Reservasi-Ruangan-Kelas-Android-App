package id.alian.reservasikelas.view.ui.dosen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.alian.reservasikelas.databinding.ActivityEditProfileDosenBinding

class EditProfileDosenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileDosenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileDosenBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}