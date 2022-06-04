package id.alian.reservasikelas.view.ui.ruangan

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.alian.reservasikelas.R
import id.alian.reservasikelas.databinding.ActivityListRuanganBinding
import id.alian.reservasikelas.view.adapter.ListRuanganAdapter
import id.alian.reservasikelas.view.callback.Resource
import id.alian.reservasikelas.view.callback.openActivity
import id.alian.reservasikelas.view.callback.shortSnackBar
import id.alian.reservasikelas.viewmodel.RuanganViewModel
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ListRuanganActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListRuanganBinding

    private val viewModel: RuanganViewModel by viewModels()
    private val adapter: ListRuanganAdapter by lazy { ListRuanganAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListRuanganBinding.inflate(layoutInflater)
        setupRecyclerView()
        updateUIState()
        with(binding) {
            setContentView(root)
            toolbar.setNavigationOnClickListener {
                finish()
            }
            swipeUpRefresh.setOnRefreshListener {
                lifecycleScope.launchWhenCreated {
                    viewModel.getDataRuangan()
                }
            }
        }
        adapter.setOnItemClickListener { ruangan ->
            openActivity(DetailRuanganActivity::class.java,
                nameString = getString(R.string.extra_kode_ruangan),
                valueString = ruangan.kodeRuangan,
                nameInt = getString(R.string.extra_id_ruangan),
                valueInt = ruangan.id)
        }
    }

    private fun setupRecyclerView() {
        with(binding) {
            ruanganRecyclerView.adapter = adapter
            ruanganRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        }
    }

    private fun updateUIState() {
        lifecycleScope.launchWhenCreated {
            viewModel.ruangan.collectLatest {
                when (it) {
                    is Resource.Success -> {
                        hideLoading()
                        adapter.differ.submitList(it.data?.data)
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
            swipeUpRefresh.isRefreshing = true
        }
    }

    private fun hideLoading() {
        with(binding) {
            swipeUpRefresh.isRefreshing = false
        }
    }
}