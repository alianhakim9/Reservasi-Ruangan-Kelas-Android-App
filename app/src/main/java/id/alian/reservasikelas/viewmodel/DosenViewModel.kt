package id.alian.reservasikelas.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.alian.reservasikelas.service.model.Dosen
import id.alian.reservasikelas.service.repository.DosenRepository
import id.alian.reservasikelas.service.response.BaseResponse
import id.alian.reservasikelas.view.callback.Constants
import id.alian.reservasikelas.view.callback.Resource
import id.alian.reservasikelas.view.callback.checkInternetConnection
import id.alian.reservasikelas.view.callback.getReservasiSharedPref
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DosenViewModel @Inject constructor(
    private val repository: DosenRepository,
    private val app: Application,
) : AndroidViewModel(app) {
    // state
    private var _login = MutableStateFlow<Resource<BaseResponse<Dosen>>>(Resource.Empty())
    val login: StateFlow<Resource<BaseResponse<Dosen>>> = _login

    private var _profile = MutableLiveData<Resource<BaseResponse<Dosen>>>()
    val profile: LiveData<Resource<BaseResponse<Dosen>>> = _profile

    val sharedPref = app.getReservasiSharedPref()
    private val nidn = sharedPref.getString("nidn", null)

    fun login(nidn: String, password: String) {
        _login.value = Resource.Loading()
        if (nidn.isNotEmpty() && password.isNotEmpty()) {
            if (nidn.length < 10) {
                _login.value = Resource.Error(Constants.NOT_VALID_NIM)
            } else {
                if (app.checkInternetConnection()) {
                    try {
                        viewModelScope.launch {
                            val response = repository.login(nidn, password)
                            if (response.isSuccessful) {
                                response.body()?.let {
                                    _login.value = Resource.Success(it)
                                }
                            } else {
                                if (response.code() == 400) {
                                    _login.value = Resource.Error(Constants.LOGIN_FAILED_DOSEN)
                                } else {
                                    _login.value = Resource.Error(Constants.SERVER_ERROR)
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.i(Constants.TAG, "login: ${e.localizedMessage}")
                    }
                } else {
                    _login.value = Resource.Error(Constants.NO_INTERNET_CONNECTION)
                }
            }
        } else {
            _login.value = Resource.Error(Constants.DATA_EMPTY_MESSAGE)
        }
    }

    fun getProfile() {
        if (nidn != null) {
            if (app.checkInternetConnection()) {
                _profile.postValue(Resource.Loading())
                try {
                    viewModelScope.launch {
                        val response = repository.getProfile(nidn)
                        if (response.isSuccessful) {
                            response.body()?.let {
                                _profile.postValue(Resource.Success(it))
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.i(Constants.TAG, "getProfile: ${e.localizedMessage}")
                }
            } else {
                _profile.postValue(Resource.Error(Constants.NO_INTERNET_CONNECTION))
            }
        }
    }
}