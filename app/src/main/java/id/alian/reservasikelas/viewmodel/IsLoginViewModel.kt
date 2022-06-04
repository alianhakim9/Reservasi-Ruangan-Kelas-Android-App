package id.alian.reservasikelas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.alian.reservasikelas.view.callback.getReservasiSharedPref
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IsLoginViewModel @Inject constructor(
    app: Application,
) : AndroidViewModel(app) {
    private var _isLogin = MutableLiveData<Boolean>()

    //    private var _profileDosen = MutableLiveData<Boolean>()
    private var _profileDosen = MutableLiveData<Boolean>()
    val profileDosen: LiveData<Boolean> = _profileDosen
    private var _profileMhs = MutableLiveData<Boolean>()
    val profileMhs: LiveData<Boolean> = _profileMhs

    val isLogin: LiveData<Boolean> = _isLogin
    private val nidn = app.getReservasiSharedPref().getString("nidn", null)
    private val nim = app.getReservasiSharedPref().getString("nim", null)
    private val sharedPref = app.getReservasiSharedPref()

    init {
        checkLogin()
    }

    private fun checkLogin() {
        if (nidn != null || nim != null) {
            _isLogin.postValue(true)
        }
    }

    fun profile() {
        if (nidn != null) {
            _profileDosen.value = true
        } else if (nim != null) {
            _profileMhs.postValue(true)
        }
    }

    fun logout() {
        viewModelScope.launch {
            sharedPref.edit().clear().apply()
        }
    }
}