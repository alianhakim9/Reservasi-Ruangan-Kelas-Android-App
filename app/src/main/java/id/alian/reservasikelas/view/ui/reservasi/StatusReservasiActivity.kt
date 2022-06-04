package id.alian.reservasikelas.view.ui.reservasi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.alian.reservasikelas.databinding.ActivityStatusReservasiBinding
import id.alian.reservasikelas.view.adapter.ListBookRuanganAdapter
import id.alian.reservasikelas.view.adapter.ListRuanganAdapter
import id.alian.reservasikelas.view.callback.Resource
import id.alian.reservasikelas.view.callback.shortSnackBar
import id.alian.reservasikelas.viewmodel.MahasiswaViewModel
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class StatusReservasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStatusReservasiBinding
    private val viewModel: MahasiswaViewModel by viewModels()
    private val adapter: ListBookRuanganAdapter by lazy { ListBookRuanganAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatusReservasiBinding.inflate(layoutInflater)
        with(binding)
        {
            setContentView(root)
            toolbar.setNavigationOnClickListener { finish() }
            viewModel.getStatusReservasi()
        }
        setupRecyclerView()
        updateUIState()
    }

    private fun updateUIState() {
        lifecycleScope.launchWhenCreated {
            viewModel.status.collectLatest {
                when (it) {
                    is Resource.Success -> {
                        adapter.differ.submitList(it.data?.data)
                    }

                    is Resource.Loading -> {

                    }

                    is Resource.Error -> {
                        binding.root.shortSnackBar(it.message.toString())
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupRecyclerView() {
        with(binding) {
            bookRecyclerView.adapter = adapter
            bookRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        }
    }
}