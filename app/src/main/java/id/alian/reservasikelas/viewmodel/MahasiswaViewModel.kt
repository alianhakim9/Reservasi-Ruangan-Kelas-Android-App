package id.alian.reservasikelas.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.alian.reservasikelas.service.model.BookRuangan
import id.alian.reservasikelas.service.model.Mahasiswa
import id.alian.reservasikelas.service.repository.MahasiswaRepository
import id.alian.reservasikelas.service.response.BaseResponse
import id.alian.reservasikelas.view.callback.Constants.DATA_EMPTY_MESSAGE
import id.alian.reservasikelas.view.callback.Constants.LOGIN_FAILED_MHS
import id.alian.reservasikelas.view.callback.Constants.NOT_VALID_NIM
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
class MahasiswaViewModel @Inject constructor(
    private val repository: MahasiswaRepository,
    private val app: Application,
) : AndroidViewModel(app) {
    // state
    private var _login = MutableStateFlow<Resource<BaseResponse<Mahasiswa>>>(Resource.Empty())
    val login: StateFlow<Resource<BaseResponse<Mahasiswa>>> = _login

    private var _profile =
        MutableStateFlow<Resource<BaseResponse<List<Mahasiswa>>>>(Resource.Empty())
    val profile: StateFlow<Resource<BaseResponse<List<Mahasiswa>>>> = _profile

    private var _status =
        MutableStateFlow<Resource<BaseResponse<List<BookRuangan>>>>(Resource.Empty())
    val status: StateFlow<Resource<BaseResponse<List<BookRuangan>>>> = _status

    val sharedPref = app.getReservasiSharedPref()
    private val nim = sharedPref.getString("nim", null)
    private val idMahasiswa = sharedPref.getInt("id_mahasiswa", 0)

    fun login(nim: String, password: String) {
        _login.value = Resource.Loading()
        if (nim.isNotEmpty() && password.isNotEmpty()) {
            if (nim.length < 10) {
                _login.value = Resource.Error(NOT_VALID_NIM)
            } else {
                if (app.checkInternetConnection()) {
                    try {
                        viewModelScope.launch {
                            val response = repository.loginMahasiswa(nim, password)
                            if (response.isSuccessful) {
                                response.body()?.let {
                                    _login.value = Resource.Success(it)
                                }
                            } else {
                                if (response.code() == 400) {
                                    _login.value = Resource.Error(LOGIN_FAILED_MHS)
                                } else {
                                    _login.value = Resource.Error(SERVER_ERROR)
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.i(TAG, "login: ${e.localizedMessage}")
                    }
                } else {
                    _login.value = Resource.Error(NO_INTERNET_CONNECTION)
                }
            }
        } else {
            _login.value = Resource.Error(DATA_EMPTY_MESSAGE)
        }
    }

    fun getProfile() {
        if (nim != null) {
            if (app.checkInternetConnection()) {
                _profile.value = Resource.Loading()
                try {
                    viewModelScope.launch {
                        val response = repository.getProfile(nim)
                        if (response.isSuccessful) {
                            response.body()?.let { mhs ->
                                mhs.data.forEach {
                                    sharedPref.edit().putInt("id_mahasiswa", it.id!!).apply()
                                }
                                _profile.value = Resource.Success(mhs)
                            }
                        } else {
                            if (response.code() == 400) {
                                _profile.value = Resource.Error(LOGIN_FAILED_MHS)
                            } else {
                                _profile.value = Resource.Error(SERVER_ERROR)
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.i(TAG, "getProfile: ${e.localizedMessage}")
                }
            } else {
                _profile.value = Resource.Error(NO_INTERNET_CONNECTION)
            }
        }
    }

    fun getStatusReservasi() {
        _status.value = Resource.Loading()
        if (idMahasiswa != 0) {
            if (app.checkInternetConnection()) {
                viewModelScope.launch {
                    try {
                        val response = repository.getStatusReservasi(idMahasiswa)
                        if (response.isSuccessful) {
                            response.body()?.let {
                                _status.value = Resource.Success(it)
                            }
                        } else {
                            _status.value = Resource.Error(SERVER_ERROR)
                        }
                    } catch (e: Exception) {
                        Log.i(TAG, "getStatusReservasi: $e")
                        _status.value = Resource.Error(e.message.toString())
                    }
                }
            } else {
                _status.value = Resource.Error(NO_INTERNET_CONNECTION)
            }
        }
    }
}