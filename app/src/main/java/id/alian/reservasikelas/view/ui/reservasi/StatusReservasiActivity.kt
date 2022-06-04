package id.alian.reservasikelas.view.ui.reservasi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import id.alian.reservasikelas.databinding.ActivityStatusReservasiBinding

@AndroidEntryPoint
class StatusReservasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStatusReservasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatusReservasiBinding.inflate(layoutInflater)
        with(binding)
        {
            setContentView(root)
            toolbar.setNavigationOnClickListener { finish() }

        }
    }
}