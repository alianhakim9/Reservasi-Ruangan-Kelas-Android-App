package id.alian.reservasikelas.service.repository

import id.alian.reservasikelas.service.ReservasiService
import javax.inject.Inject

class DosenRepository @Inject constructor(private val api: ReservasiService) {
    suspend fun login(nidn: String, password: String) = api.loginDosen(nidn, password)

    suspend fun getProfile(nidn: String) = api.getProfileDosen(nidn)
}