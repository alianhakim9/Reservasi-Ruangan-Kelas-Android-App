package id.alian.reservasikelas.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.alian.reservasikelas.service.model.BookRuangan
import id.alian.reservasikelas.service.model.Dosen
import id.alian.reservasikelas.service.model.MataKuliah
import id.alian.reservasikelas.service.model.Ruangan
import id.alian.reservasikelas.service.repository.RuanganRepository
import id.alian.reservasikelas.service.response.BaseResponse
import id.alian.reservasikelas.view.callback.Constants.DATA_EMPTY_MESSAGE
import id.alian.reservasikelas.view.callback.Constants.NO_INTERNET_CONNECTION
import id.alian.reservasikelas.view.callback.Constants.SERVER_ERROR
import id.alian.reservasikelas.view.callback.Constants.TAG
import id.alian.reservasikelas.view.callback.Resource
import id.alian.reservasikelas.view.callback.checkInternetConnection
import id.alian.reservasikelas.view.callback.getReservasiSharedPref
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RuanganViewModel @Inject constructor(
    private val repository: RuanganRepository,
    private val app: Application,
) : AndroidViewModel(app) {

    private var _ruangan = MutableStateFlow<Resource<BaseResponse<List<Ruangan>>>>(Resource.Empty())
    val ruangan: StateFlow<Resource<BaseResponse<List<Ruangan>>>> = _ruangan
    private var _dosen = MutableStateFlow<Resource<BaseResponse<List<Dosen>>>>(Resource.Empty())
    val dosen: StateFlow<Resource<BaseResponse<List<Dosen>>>> = _dosen
    private var _mataKuliah =
        MutableStateFlow<Resource<BaseResponse<List<MataKuliah>>>>(Resource.Empty())
    val mataKuliah: StateFlow<Resource<BaseResponse<List<MataKuliah>>>> = _mataKuliah
    private var _bookRuangan =
        MutableStateFlow<Resource<BaseResponse<BookRuangan>>>(Resource.Empty())
    val bookRuangan: StateFlow<Resource<BaseResponse<BookRuangan>>> = _bookRuangan
    val sharedPref = app.getReservasiSharedPref()

    init {
        getDataRuangan()
    }

    fun getDataRuangan() {
        _ruangan.value = Resource.Loading()
        if (app.checkInternetConnection()) {
            viewModelScope.launch {
                try {
                    val response = repository.getDataRuangan()
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _ruangan.value = Resource.Success(it)
                        }
                    } else {
                        Log.i(TAG, "getDataRuangan: ${response.code()}")
                        _ruangan.value = Resource.Error(SERVER_ERROR)
                    }
                } catch (e: Exception) {
                    Log.i(TAG, "getDataRuangan: ${e.localizedMessage}")
                    _ruangan.value = Resource.Error(SERVER_ERROR)
                }
            }
        } else {
            _ruangan.value = Resource.Error(NO_INTERNET_CONNECTION)
        }
    }

    fun getDataMataKuliah() {
        _mataKuliah.value = Resource.Loading()
        try {
            viewModelScope.launch {
                val response = repository.getDataMataKuliah()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _mataKuliah.value = Resource.Success(it)
                    }
                }
            }
        } catch (e: Exception) {
            _mataKuliah.value = Resource.Error(SERVER_ERROR)
            Log.i(TAG, "getDataMataKuliah: ${e.localizedMessage}")
        }
    }

    fun getDataDosen() {
        _dosen.value = Resource.Loading()
        try {
            viewModelScope.launch {
                val response = repository.getDataDosen()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _dosen.value = Resource.Success(it)
                    }
                }
            }
        } catch (e: Exception) {
            _dosen.value = Resource.Error(SERVER_ERROR)
            Log.i(TAG, "getDataMataKuliah: ${e.localizedMessage}")
        }
    }

    fun bookRuangan(bookRuangan: BookRuangan) {
        _bookRuangan.value = Resource.Loading()
        if (app.checkInternetConnection()) {
            with(bookRuangan) {
                if (idDosen == 0 || idMahasiswa == 0 || idMataKuliah == 0 || tanggal.isEmpty() || jamAwal.isEmpty() || jamAkhir.isEmpty()) {
                    _bookRuangan.value = Resource.Error(DATA_EMPTY_MESSAGE)
                } else {
                    viewModelScope.launch {
                        try {
                            val response = repository.bookRuangan(bookRuangan)
                            if (response.isSuccessful) {
                                response.body()?.let {
                                    _bookRuangan.value = Resource.Success(it)
                                }
                            } else {
                                Log.i(TAG, "bookRuangan: $response")
                                _bookRuangan.value = Resource.Error(SERVER_ERROR)
                            }
                        } catch (e: java.lang.Exception) {
                            Log.i(TAG, "bookRuangan: $e")
                            _bookRuangan.value = Resource.Error(SERVER_ERROR)
                        }
                    }
                }
            }
        } else {
            _bookRuangan.value = Resource.Error(NO_INTERNET_CONNECTION)
        }
    }
}